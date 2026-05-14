package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.entities.Role;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.uaa.dto.RoleData;
import tz.co.divinesolutions.tenants_backend.uaa.dto.RoleDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleService {
    Response<Role> saveRole(RoleDto dto);

    Response<RoleData> findAllRoles();

    Response<RoleData> findByUid(UUID uid);

    Optional<Role> getOptionalByUid(UUID uid);

    Optional<Role> getOptionalByName(String name);

    List<Role> findAllByUidIn(Set<UUID> roleUids);

    Role saveRole(Role role);
}
