package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.entities.Permission;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.uaa.dto.PermissionDto;

import java.util.Set;

public interface PermissionService {
    Set<Permission> findAllByIdsIn(Set<Long> permissionIds);
    Response<PermissionDto> getPermissionList();
}
