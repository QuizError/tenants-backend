package tz.co.divinesolutions.tenants_backend.uaa.controller;

import tz.co.divinesolutions.tenants_backend.entities.Role;
import tz.co.divinesolutions.tenants_backend.globals.*;
import tz.co.divinesolutions.tenants_backend.uaa.dto.*;
import tz.co.divinesolutions.tenants_backend.uaa.service.PermissionService;
import tz.co.divinesolutions.tenants_backend.uaa.service.RoleService;
import tz.co.divinesolutions.tenants_backend.uaa.service.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.uaa.service.UserPhotoService;

import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserAccountService userAccountService;
    private final PermissionService permissionService;
    private final UserPhotoService userPhotoService;
    private final PageableHelper pageableHelper;
    private final RoleService roleService;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW_USERS_LIST')")
    public ResponseEntity<Response<PageResponse<UserAccountData>>> searchUsers(
            @RequestBody PageableParam pageableParam) {
        Page<UserAccountData> usersPage = userAccountService.searchUsers(pageableParam);

        PageResponse<UserAccountData> responseData = pageableHelper.toPageResponse(usersPage);

        return ResponseEntity.ok(
                new Response<>(true, ResponseCode.SUCCESS,
                        "Users retrieved successfully", responseData)
        );
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE_USER')")
    public Response<UserAccountData> saveNewUser(@Valid @RequestBody UserDto userDto) {
        return userAccountService.createNewUser(userDto);
    }

    @PostMapping("/change-type")
    @PreAuthorize("hasAuthority('ROLE_EDIT_USER')")
    public Response<UserAccountData> saveNewUser(@Valid @RequestBody UserTypeDto dto) {
        return userAccountService.changeUserType(dto);
    }

    @PostMapping("/update-profile")
    @PreAuthorize("hasAuthority('ROLE_EDIT_USER')")
    public Response<UserAccountData> updateProfileData(@Valid @RequestBody UserDto dto) {
        return userAccountService.updateUserProfile(dto);
    }

    @PostMapping("/update-dp")
    @PreAuthorize("hasAuthority('ROLE_EDIT_USER')")
    public Response<UserPhotoData> changeProfilePhoto(@Valid @RequestBody UserPhotoDto dto) {
        try {
            return userPhotoService.updateProfileImage(dto);
        }
        catch (Exception e){
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    e.getMessage(),
                    null
            );
        }
    }

    @GetMapping("/image/{uid}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_USER')")
    public Response<UserPhotoData> getPhotoByUid(@PathVariable UUID uid) {
        return userPhotoService.getPhotoByUid(uid);
    }

    @PostMapping("/create-role")
    @PreAuthorize("hasAuthority('ROLE_CREATE_NEW_ROLE')")
    public Response<Role> saveRole(@Valid @RequestBody RoleDto roleDto) {
        return roleService.saveRole(roleDto);
    }

    @GetMapping("/list-roles")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLES')")
    public Response<RoleData> findAllRoles() {
        return roleService.findAllRoles();
    }

    @GetMapping("/get-role/{uid}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ROLE')")
    public Response<RoleData> findByUid(@PathVariable UUID uid) {
        return roleService.findByUid(uid);
    }

    @PostMapping("/assign-roles")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN_ROLES_TO_USER')")
    public Response<AssignRolesResponse> assignRolesToUser(
            @Valid @RequestBody AssignRolesDto assignRolesDto) {
        return userAccountService.assignRolesToUser(assignRolesDto);
    }

    @PostMapping("/assign-permissions")
    @PreAuthorize("hasAuthority('ROLE_ASSIGN_PERMISSIONS_TO_ROLE')")
    public Response<AssignPermissionsToRoleResponse> assignPermissionsToRole(
            @Valid @RequestBody AssignPermissionsToRoleDto assignPermissionsToRoleDto) {
        return userAccountService.assignPermissionsToRole(assignPermissionsToRoleDto);
    }

    @GetMapping("/permissions-list")
    @PreAuthorize("hasAuthority('ROLE_VIEW_ALL_PERMISSIONS')")
    public Response<PermissionDto> getPermissionList() {
        return permissionService.getPermissionList();
    }
}