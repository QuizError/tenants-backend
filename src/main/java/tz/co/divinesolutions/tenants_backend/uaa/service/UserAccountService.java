package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.globals.PageableParam;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordRequest;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordResponse;
import tz.co.divinesolutions.tenants_backend.uaa.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserAccountService {
    Response<UserAccountData> createNewUser(UserDto dto);

    Response<UserAccountData> changeUserType(UserTypeDto dto);

    Response<UserAccountData> updateUserProfile(UserDto dto);

    Response<ForgotPasswordResponse> forgotPasswordRequest(ForgotPasswordRequest dto);

    Page<UserAccountData> searchUsers(PageableParam pageableParam);

    Response<AssignRolesResponse> assignRolesToUser(AssignRolesDto dto);

    Response<AssignPermissionsToRoleResponse> assignPermissionsToRole(AssignPermissionsToRoleDto dto);

    Response<TokenResponse> login(LoginRequest loginRequest);

    Response<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request);

    ResponseEntity<?> logout(LogoutRequest request, HttpServletRequest httpRequest);

    ResponseEntity<?> logoutFromAllDevices(LogoutRequest request);
}
