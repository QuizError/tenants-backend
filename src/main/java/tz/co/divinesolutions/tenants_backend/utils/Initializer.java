package tz.co.divinesolutions.tenants_backend.utils;

import tz.co.divinesolutions.tenants_backend.entities.Permission;
import tz.co.divinesolutions.tenants_backend.entities.Role;
import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import tz.co.divinesolutions.tenants_backend.enums.Gender;
import tz.co.divinesolutions.tenants_backend.enums.OrganizationType;
import tz.co.divinesolutions.tenants_backend.enums.UserType;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service.CountryService;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service.GeographicAreasService;
import tz.co.divinesolutions.tenants_backend.uaa.repository.PermissionRepository;
import tz.co.divinesolutions.tenants_backend.uaa.repository.RoleRepository;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tz.co.divinesolutions.tenants_backend.enums.PermissionScope.ORGANIZATION;
import static tz.co.divinesolutions.tenants_backend.enums.PermissionScope.SYSTEM;
import static tz.co.divinesolutions.tenants_backend.utils.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryService countryService;
    private final PermissionRepository permissionRepository;
    private final UserAccountRepository userAccountRepository;
    private final GeographicAreasService geographicAreasService;

    public static final String USER_MANAGEMENT = "USER MANAGEMENT";
    public static final String GEOGRAPHICAL_AREAS = "GEOGRAPHICAL AREAS";
    public static final String CURRENCY_MANAGEMENT = "CURRENCY MANAGEMENT";
    public static final String GROUP_MANAGEMENT = "OWNERSHIP GROUPS";
    public static final String ORGANIZATION_MANAGEMENT = "ORGANIZATION MANAGEMENT";

    @Override
    public void run(String @NonNull ... args) throws Exception {
        seedRoles();
        seedPermissions();
        seedDefaultUser();
        countryService.seed();
        seedSuperAdminPermissions();
        geographicAreasService.seedTanzaniaRegions();
    }

    public void seedRoles(){
        log.info("**** BEGIN SEEDING SYSTEM DEFAULT ROLES");
        List<String> createdRoles = new ArrayList<>();
        if (!roleRepository.existsByNameAndActiveTrue(ROLE_SUPER_ADMINISTRATOR)){
            Role superAdmin = new Role();
            superAdmin.setCreatedBy(1L);
            superAdmin.setIsSystemRole(true);
            superAdmin.setOrganizationType(OrganizationType.SYSTEM);
            superAdmin.setName(ROLE_SUPER_ADMINISTRATOR);
            superAdmin.setDisplayName("SUPER ADMINISTRATOR");
            superAdmin.setDescription("Has all permissions to support the whole system");
            Role savedRole = roleRepository.save(superAdmin);
            createdRoles.add(savedRole.getName());
        }
        if (!roleRepository.existsByNameAndActiveTrue(ROLE_GUEST)){
            Role guestRole = new Role();
            guestRole.setCreatedBy(1L);
            guestRole.setName(ROLE_GUEST);
            guestRole.setIsSystemRole(true);
            guestRole.setOrganizationType(OrganizationType.SYSTEM);
            guestRole.setDisplayName("GUEST USER");
            guestRole.setDescription("Has few permissions given to user on registration");
            Role savedGuestRole = roleRepository.save(guestRole);
            createdRoles.add(savedGuestRole.getName());
        }
        if (!roleRepository.existsByNameAndActiveTrue(ROLE_AGENT)){
            Role guestRole = new Role();
            guestRole.setCreatedBy(1L);
            guestRole.setName(ROLE_AGENT);
            guestRole.setIsSystemRole(true);
            guestRole.setOrganizationType(OrganizationType.SYSTEM);
            guestRole.setDisplayName("PROPERTY AGENT");
            guestRole.setDescription("Has few permissions given to user on registration");
            Role savedGuestRole = roleRepository.save(guestRole);
            createdRoles.add(savedGuestRole.getName());
        }
        if (!roleRepository.existsByNameAndActiveTrue(ROLE_OWNER)){
            Role guestRole = new Role();
            guestRole.setCreatedBy(1L);
            guestRole.setName(ROLE_OWNER);
            guestRole.setIsSystemRole(true);
            guestRole.setOrganizationType(OrganizationType.SYSTEM);
            guestRole.setDisplayName("PROPERTY OWNER");
            guestRole.setDescription("Has few permissions given to user on registration");
            Role savedGuestRole = roleRepository.save(guestRole);
            createdRoles.add(savedGuestRole.getName());
        }
        if (!createdRoles.isEmpty()){
            log.info("{} roles have been seeded", createdRoles.size());
        }
        log.info("SEEDING DEFAULT ROLES COMPLETED *****");
    }

    public void seedPermissions(){
        log.info("***** BEGIN SEEDING PERMISSIONS");
        List<String> createdPermissions = new ArrayList<>();

        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission("ROLE_CREATE_USER", "Can create user account", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_EDIT_USER", "Can edit user account data", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_USER", "Can view user account  details", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_USERS_LIST", "Can view a list of user accounts", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_DELETE_USER", "Can delete/deactivate user account", USER_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_MAKE_USER_SUPER_ADMIN", "Can make and remove user to be super admin", USER_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_VIEW_ALL_PERMISSIONS","Can view permission list",USER_MANAGEMENT,ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_ROLES","Can view list of roles",USER_MANAGEMENT,ORGANIZATION));
        permissions.add(new Permission("ROLE_CREATE_NEW_ROLE","Can create new role",USER_MANAGEMENT,ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_ROLE","Can view role details and assigned permissions",USER_MANAGEMENT,ORGANIZATION));

        //        Role Creation and Assignment Permissions
        permissions.add(new Permission("ROLE_CREATE_ROLE", "Can create system role", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_EDIT_ROLE", "Can edit system role", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_ROLE", "Can view system role", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_ROLE_LIST", "Can view a list of system roles", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_DELETE_ROLE", "Can delete/deactivate system created role", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_ASSIGN_ROLES_TO_USER", "Can assign selected roles to user account", USER_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_ASSIGN_PERMISSIONS_TO_ROLE", "Can assign permissions to selected role", USER_MANAGEMENT, ORGANIZATION));

        //        Geographical Areas Management Permissions
        permissions.add(new Permission("ROLE_VIEW_COUNTRIES", "Can view seeded countries", GEOGRAPHICAL_AREAS, ORGANIZATION));
        permissions.add(new Permission("ROLE_SEED_GEOGRAPHICAL_AREAS", "Can seed regions through villages", GEOGRAPHICAL_AREAS, SYSTEM));
        permissions.add(new Permission("ROLE_VIEW_GEOGRAPHICAL_AREAS", "Can view regions through villages", GEOGRAPHICAL_AREAS, ORGANIZATION));

        //        Currency Management Permissions
        permissions.add(new Permission("ROLE_CREATE_CURRENCY", "Can create new currency in the system", CURRENCY_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_EDIT_CURRENCY", "Can edit currency already created in the system", CURRENCY_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_VIEW_CURRENCIES", "Can view a list of currencies", CURRENCY_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_VIEW_CURRENCY", "Can view currency details", CURRENCY_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_DELETE_CURRENCY", "Can delete currency", CURRENCY_MANAGEMENT, SYSTEM));

        //        Group Ownership Management Permissions
        permissions.add(new Permission("ROLE_CREATE_OWNERSHIP_GROUP", "Can create new ownership group in the system", GROUP_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_EDIT_OWNERSHIP_GROUP", "Can edit ownership group data in the system", GROUP_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_VIEW_OWNERSHIP_GROUPS", "Can view a list of ownership groups", GROUP_MANAGEMENT, SYSTEM));
        permissions.add(new Permission("ROLE_VIEW_OWNERSHIP_GROUP", "Can view ownership group details", GROUP_MANAGEMENT, ORGANIZATION));
        permissions.add(new Permission("ROLE_DELETE_OWNERSHIP_GROUP", "Can delete ownership group from the system", GROUP_MANAGEMENT, SYSTEM));

        for (Permission permission : permissions){
            if (!permissionRepository.existsByNameAndActiveTrue(permission.getName())){
                permission.setCreatedBy(1L);
                permissionRepository.save(permission);
                createdPermissions.add(permission.getName());
                log.info("-->>> Permission {} have been seeded at {}: *****",permission.getName(), LocalDateTime.now());
            }
        }
        if (!createdPermissions.isEmpty()){
            log.info("{} new permissions have been seeded", createdPermissions.size());
        }
        log.info("END SEEDING PERMISSIONS *****");
    }

    public void seedDefaultUser(){
        log.info("***** SEEDING DEFAULT USER (SYSADMIN)");
        if (userAccountRepository.findAll().isEmpty()){
            UserAccount systemAdmin = new UserAccount();
            systemAdmin.setFirstname("Super");
            systemAdmin.setMiddleName("System");
            systemAdmin.setLastname("Administrator");
            systemAdmin.setFullName("Super Administrator");
            systemAdmin.setEmail("admin@loan.com");
            systemAdmin.setMsisdn("255788100200");
            systemAdmin.setUsername("0788100200");
            systemAdmin.setSuperAdmin(true);
            systemAdmin.setVerified(true);
            systemAdmin.setUserType(UserType.INTERNAL);
            systemAdmin.setPassword(passwordEncoder.encode("1000"));
            systemAdmin.setGender(Gender.Male);
            systemAdmin.setCreatedBy(1L);
            userAccountRepository.save(systemAdmin);
        }
        log.info("System Admin seeded successfully *****");
    }

    public void seedSuperAdminPermissions() {

        roleRepository.findByName(ROLE_SUPER_ADMINISTRATOR).ifPresent(role -> {

            Set<Permission> permissions = new HashSet<>(permissionRepository.findAll());

            role.setPermissions(permissions);
            roleRepository.save(role);

            userAccountRepository.findByUsername("0788100200").ifPresent(user -> {

                user.setRoles(Set.of(role));
                userAccountRepository.save(user);

            });

        });
    }
}