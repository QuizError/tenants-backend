package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.entities.Permission;
import tz.co.divinesolutions.tenants_backend.entities.Role;
import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import tz.co.divinesolutions.tenants_backend.enums.UserType;
import tz.co.divinesolutions.tenants_backend.globals.*;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordRequest;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordResponse;
import tz.co.divinesolutions.tenants_backend.sms.dto.Recipient;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;
import tz.co.divinesolutions.tenants_backend.sms.service.SMSService;
import tz.co.divinesolutions.tenants_backend.uaa.dto.*;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserAccountRepository;
import tz.co.divinesolutions.tenants_backend.utils.JwtUtils;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tz.co.divinesolutions.tenants_backend.utils.Constants.ROLE_GUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    private final UserAccountRepository userAccountRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;
    private final RoleService roleService;
    private final SMSService smsService;
    private final LoggedUser loggedUser;
    private final JwtUtils jwtUtils;

    @Override
    public Response<UserAccountData> createNewUser(UserDto dto) {
        try {
            log.info("User saving with dto: {}", dto);
            if (dto.getFirstname() == null || dto.getFirstname().isEmpty() || dto.getLastname() == null || dto.getLastname().isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.MISSING_DATA,
                        "Firstname and Lastname can not be empty",
                        null);
            }

            if (userAccountRepository.existsByMsisdn(getMsisdnFromMobile(dto.getMobile()))){
                return new Response<>(
                        false,
                        ResponseCode.DUPLICATE_RECORD,
                        "We already have a user with this mobile number please try another number or login to proceed",
                        null);
            }
            if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
                if (userAccountRepository.existsByEmailAndEmailIsNotNull(dto.getEmail())) {
                    return new Response<>(
                            false,
                            ResponseCode.DUPLICATE_RECORD,
                            "This email is already registered please use a different email",
                            null
                    );
                }
            }
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUid(dto.getUid());
            if (dto.getUid() != null && optionalUserAccount.isEmpty()){
                return new Response<>(false,ResponseCode.NO_RECORD_FOUND,"User could not be found or may have been deleted from the system", null);
            }

            String email = dto.getEmail() != null && !dto.getEmail().isEmpty() ? dto.getEmail().trim().toLowerCase() : dto.getEmail();
            String password = dto.getPassword() != null && !dto.getPassword().isEmpty() ? dto.getPassword() : "1234";
            UserType userType = dto.getUserType() != null ? dto.getUserType() : UserType.GUEST_USER;

            Optional<Role> optionalRole = roleService.getOptionalByName(ROLE_GUEST);
            if (optionalRole.isPresent()){
                Role guestRole = optionalRole.get();

                UserAccount userAccount = optionalUserAccount.orElse(new UserAccount());
                userAccount.setEmail(email);
                userAccount.setUserType(userType);
                userAccount.setMsisdn(getMsisdnFromMobile(dto.getMobile()));
                userAccount.setGender(dto.getGender());
                userAccount.setRoles(Set.of(guestRole));
                userAccount.setUsername(dto.getMobile());
                userAccount.setLastname(dto.getLastname());
                userAccount.setFirstname(dto.getFirstname());
                userAccount.setFullName(resolveFullName(dto));
                userAccount.setMiddleName(dto.getMiddleName());
                userAccount.setPassword(passwordEncoder.encode(password));
                UserAccount savedUser = userAccountRepository.save(userAccount);

                //saved created by (self referencing)
                if (loggedUser.isAuthenticated()) {
                    Long currentUserId = loggedUser.getCurrentUserId();
                    log.info("User is being created by: {}", currentUserId);
                    savedUser.setCreatedBy(currentUserId);
                }

                userAccountRepository.save(savedUser);

                return new Response<>(
                        true,
                        ResponseCode.SUCCESS,
                        "Registration was successful your default password is 1234",
                        convertToDto(savedUser));
            }
            else {
                return new Response<>(
                        false,
                        ResponseCode.GUEST_ROLE_NOT_FOUND,
                        "Registration could not proceed kindly contact support",
                        null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Error when saving user account",
                    null);
        }
    }

    @Override
    public Response<UserAccountData> changeUserType(UserTypeDto dto){
        try {
            if(loggedUser.getCurrentUser() == null){
                return new Response<>(
                        false,
                        ResponseCode.UNAUTHORIZED,
                        "Full authentication is required for this action",
                        null);
            }
            UserAccount userAccount = loggedUser.getCurrentUser();

            log.info("***** Selected user type fot role is: {}", dto.getUserType().name());

            Optional<Role> optionalRole = roleService.getOptionalByName(dto.getUserType().name());
            if (optionalRole.isPresent()) {
                Role selectedRole = optionalRole.get();
                userAccount.setUserType(dto.getUserType());
                userAccount.setRoles(Set.of(selectedRole));
                UserAccount saved =  userAccountRepository.save(userAccount);
                return new Response<>(
                        true,
                        ResponseCode.SUCCESS,
                        "User type updated successfully",
                        convertToDto(saved));
            }

            return new Response<>(
                    false,
                    ResponseCode.INVALID_INPUT_DATA,
                    "Selected role could not be found or may have not been seeded",
                    null);
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Failure when updating your user type kindly contact support",
                    null);
        }
    }

    @Override
    public Response<UserAccountData> updateUserProfile(UserDto dto){
        if (dto.getUid() == null){
            return new Response<>(
                    false,
                    ResponseCode.MISSING_DATA,
                    "User uid must be provided for user profile update",
                    null);
        }

        Optional<UserAccount> optionalExistingUserAccount = userAccountRepository.findFirstByUid(dto.getUid());
        if (optionalExistingUserAccount.isEmpty()){
            return new Response<>(
                    false,
                    ResponseCode.NO_RECORD_FOUND,
                    "User account could not be found or may have been deleted from the system",
                    null);
        }
        //updating user account data (profile)
        UserAccount userAccount = optionalExistingUserAccount.get();

        String email = dto.getEmail() != null && !dto.getEmail().isEmpty() ? dto.getEmail().trim().toLowerCase() : dto.getEmail();
        userAccount.setDob(dto.getDob() != null ? LocalDate.parse(dto.getDob()) : null);
        userAccount.setFullName(resolveFullName(dto));
        userAccount.setMiddleName(dto.getMiddleName());
        userAccount.setFirstname(dto.getFirstname());
        userAccount.setIdNumber(dto.getIdNumber());
        userAccount.setLastname(dto.getLastname());
        userAccount.setIdType(dto.getIdType());
        userAccount.setGender(dto.getGender());
        userAccount.setEmail(dto.getEmail());
        userAccount.setEmail(email);
        UserAccount newProfile = userAccountRepository.save(userAccount);
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                "Profile updated successfully",
                convertToDto(newProfile)
        );
    }

    @Override
    public Response<ForgotPasswordResponse> forgotPasswordRequest(ForgotPasswordRequest dto){
        if (userAccountRepository.existsByMsisdnAndFirstnameIgnoreCase(getMsisdnFromMobile(dto.getMsisdn()), dto.getFirstname())){
            ForgotPasswordResponse response = new ForgotPasswordResponse();

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findByUsername(dto.getMsisdn());
            if (optionalUserAccount.isPresent()){
                String newPassword = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
                UserAccount userAccount = optionalUserAccount.get();
                userAccount.setPassword(passwordEncoder.encode(newPassword));
                userAccountRepository.save(userAccount);
                String message = "Hello "+ userAccount.getFullName()+" you have successfully changed your password to "+ newPassword;
                //Create SMS DTO
                SMSDto smsDto = new SMSDto();

                //Construct Recipient Object
                Recipient recipient = new Recipient();
                recipient.setRecipient_id(1);
                recipient.setDest_addr(userAccount.getMsisdn());

                smsDto.setMessage(message);
                smsDto.setSourceAddr("HOMES APP");
                smsDto.setRecipients(List.of(recipient));

                //Send SMS
                smsService.sendSms(smsDto);

                //set response body
                response.setPassword(newPassword);
                response.setMsisdn(userAccount.getMsisdn());
            }
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success",
                    response);
        }
        else {
            return new Response<>(
                    false,
                    ResponseCode.NO_RECORD_FOUND,
                    "No user found for this mobile number and firstname",
                    null);
        }
    }

    @Override
    public Page<UserAccountData> searchUsers(PageableParam pageableParam) {
        try {
            logger.info("Loading pageable list for user accounts");
            Pageable pageable = pageableHelper.buildPageable(pageableParam);

            GenericSpecificationSearch<UserAccount> genericSpec = new GenericSpecificationSearch<>();

            Specification<UserAccount> spec = Specification
                    .where(genericSpec.getSearchSpec(pageableParam.getSearchFields()))
                    .and(getUserAccountSpecs());

            Page<UserAccount> userPage = userAccountRepository.findAll(spec, pageable);

            return userPage.map(this::convertToDto);

        }
        catch (Exception e) {
            logger.error("Error in pageable user accounts search entities: ", e);
            return Page.empty();
        }
    }

    private Specification<UserAccount> getUserAccountSpecs(){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if(loggedUser.isSuperAdmin()){
                //can see all system users
                predicateList.add(criteriaBuilder.equal(root.get("enabled"), true));
            }
            //TO DO: need a logic to get all users that are for this Admin and his/ her level

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private UserAccountData convertToDto(UserAccount userAccount) {
        UserAccountData dto = new UserAccountData();
        BeanUtils.copyProperties(userAccount, dto);
        return dto;
    }

    @Override
    public Response<AssignRolesResponse> assignRolesToUser(AssignRolesDto dto) {
        try {
            Optional<UserAccount> optionalUserAccount = getOptionalByUid(dto.getUserUid());
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"Sorry the user account could not be found or may have been deleted from the system", null);
            }
            UserAccount userAccount =  optionalUserAccount.get();

            List<Role> roles = roleService.findAllByUidIn(dto.getRoleUids());

            userAccount.getRoles().clear();
            userAccount.getRoles().addAll(roles);
            userAccountRepository.save(userAccount);

            //creating response object
            AssignRolesResponse assignRoles = new AssignRolesResponse();
            assignRoles.setFullName(userAccount.getFullName());
            var authorities = userAccount.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            assignRoles.setAuthorities(authorities);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success",
                    assignRoles);
        }
        catch (Exception e){
            log.error("An error {} has occurred when assigning roles to user: ",e.getMessage());
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "An error when assigning roles to user kindly contact support ",
                    null);
        }
    }

    @Override
    public Response<AssignPermissionsToRoleResponse> assignPermissionsToRole(AssignPermissionsToRoleDto dto) {
        try {
            log.info("*** Assigning permissions with role uid: {}", dto.getRoleUid());
            Optional<Role> optionalRole = roleService.getOptionalByUid(dto.getRoleUid());
            if (optionalRole.isEmpty()){
                return new Response<>(true,ResponseCode.NO_RECORD_FOUND,"Sorry this role could not be found or may have been deleted from the system", null);
            }
            Role selectedRole = optionalRole.get();

            log.info("*** UPDATING PERMISSIONS FOR ROLE '{}' ", selectedRole.getName());
            Role updatedRole;
            selectedRole.setPermissions(permissionService.findAllByIdsIn(dto.getPermissionIds()));
            updatedRole = roleService.saveRole(selectedRole);

            log.info("Permissions for Role {} updated", updatedRole.getName());

            //Creating a response DTO

            AssignPermissionsToRoleResponse response = new AssignPermissionsToRoleResponse();
            response.setRoleName(updatedRole.getName());
            response.setAuthorities(
                    updatedRole.getPermissions()
                            .stream()
                            .map(Permission::getName)
                            .toList()
            );

            //Returning a well build response
            return new Response<>(true,ResponseCode.SUCCESS,"Permissions assigned to role successfully", response);
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "An error when assigning roles to user kindly contact support ",
                    null);
        }
    }

    public Optional<UserAccount> getOptionalByUid(UUID uid){
        return uid != null ? userAccountRepository.findFirstByUid(uid) :Optional.empty();
    }

    @Override
    public Response<TokenResponse> login(LoginRequest loginRequest) {
        try {
            // 1. Validate username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserAccount user = (UserAccount) authentication.getPrincipal();

            if (user != null){
                    updateLastLogin(user.getId(), LocalDateTime.now());
            }

            String accessToken = jwtUtils.generateAccessToken(authentication);

            assert user != null;
            String refreshToken = jwtUtils.generateRefreshToken(user.getUsername());

            // 4. Save refresh token to DATABASE
            refreshTokenService.createRefreshToken(user.getUsername(), refreshToken);

            // 5. Gather all authorities
            var authorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 6. Return both token
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setRefreshToken(refreshToken);
            tokenResponse.setTokenType("Bearer");
            tokenResponse.setExpiresIn(900L);
            tokenResponse.setExpiresAt(LocalDateTime.now().plusSeconds(900L));
            tokenResponse.setId(user.getId());
            tokenResponse.setEmail(user.getEmail());
            tokenResponse.setUsername(user.getUsername());
            tokenResponse.setAuthorities(authorities);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Login success",
                    tokenResponse);
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "An error has occurred kindly contact support",
                    null);
        }
    }

    public void updateLastLogin(Long userId, LocalDateTime now) {
        userAccountRepository.updateLastLogin(userId, now);
        userAccountRepository.updateFirstLoginIfNull(userId, now);
    }

    @Override
    public Response<TokenResponse> refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            // 1. Get username from token
            if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
                return new Response<>(
                        false,
                        ResponseCode.INVALID_INPUT_DATA,
                        "Invalid refresh token",
                        null);
            }

            String username = jwtUtils.getUserNameFromToken(refreshToken);

            // 2. check refresh token from the database
            if (!refreshTokenService.validateRefreshToken(username, refreshToken)) {
                return new Response<>(
                        false,
                        ResponseCode.INVALID_INPUT_DATA,
                        "Refresh token not found or has expired",
                        null);
            }

            // 3. Get User
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // if no authentication create new
            if (authentication == null) {
                // Need to call UserDetailsService
                // easy way. advanced way to follow
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "We are unable to process your request kindly try again",
                        null);
            }

            String newAccessToken = jwtUtils.generateAccessToken(authentication);

            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setRefreshToken(refreshToken);
            tokenResponse.setAccessToken(newAccessToken);
            tokenResponse.setExpiresAt(LocalDateTime.now().plusSeconds(900L));
            tokenResponse.setTokenType("Bearer");
            tokenResponse.setExpiresIn(900L);
            tokenResponse.setUsername(username);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success",
                    tokenResponse);
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Error when requesting for refresh token",
                    null);
        }
    }

    @Override
    public ResponseEntity<?> logout(LogoutRequest request, HttpServletRequest httpRequest) {

        String refreshToken = request.getRefreshToken();

        // 1. Validate refresh token
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body("Invalid Refresh token");
        }

        String username = jwtUtils.getUserNameFromToken(refreshToken);

        // 2. Delete refresh token from database (revoke)
        refreshTokenService.removeRefreshToken(username, refreshToken);

        // 3. Pata access token from header
        String accessToken = extractAccessTokenFromRequest(httpRequest);
        if (accessToken != null) {
            // 4. Weka access token on blacklist
            refreshTokenService.blacklistAccessToken(accessToken);
        }

        // 5. delete authentication from SecurityContext
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Log out success all tokens deleted.");
    }

    @Override
    public ResponseEntity<?> logoutFromAllDevices(LogoutRequest request) {

        String refreshToken = request.getRefreshToken();

        // 1. validate refresh token
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body("Invalid Refresh token");
        }

        String username = jwtUtils.getUserNameFromToken(refreshToken);

        // 2. delete all token for this user
        refreshTokenService.removeAllUserTokens(username);

        return ResponseEntity.ok("You have been logged out from all devices and all tokens are deleted.");
    }

    // Method to extract access token from request
    private String extractAccessTokenFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }


    public String resolveFullName(UserDto dto) {
        return Stream.of(dto.getFirstname(), dto.getMiddleName(), dto.getLastname())
                .filter(Objects::nonNull)
                .filter(name -> !name.isBlank())
                .collect(Collectors.joining(" "));
    }

    public String getMsisdnFromMobile(String mobile) {
        String cleaned = mobile.replaceAll("[^0-9]", "");

        if (cleaned.length() < 9) {
            throw new IllegalArgumentException("Mobile must have at least 9 digits");
        }

        String last9Digits = cleaned.substring(cleaned.length() - 9);

        if (!last9Digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("Last 9 characters must be numbers");
        }

        return "255" + last9Digits;
    }
}
