package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.entities.Permission;
import tz.co.divinesolutions.tenants_backend.enums.PermissionScope;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.uaa.dto.PermissionDto;
import tz.co.divinesolutions.tenants_backend.uaa.repository.PermissionRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService{

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    private final LoggedUser loggedUser;

    private final PermissionRepository permissionRepository;

    @Override
    public Set<Permission> findAllByIdsIn(Set<Long> permissionIds){
        return permissionRepository.findAllByIdIn(permissionIds);
    }


    @Override
    public Response<PermissionDto> getPermissionList() {
        try {
            logger.info("*** {}: accessing permissions list at {}", loggedUser.getCurrentUsername(),
                    LocalDateTime.now());
            List<Permission> permissions;

            if (loggedUser.isSuperAdmin()) {
                permissions = permissionRepository.findAllByActiveTrue();
            } else {
                permissions = permissionRepository.findAllByScopeAndActiveTrue(PermissionScope.ORGANIZATION);
            }

            List<PermissionDto> permissionDtoList = permissions.stream()
                    .map(permission -> {
                        PermissionDto dto = new PermissionDto();
                        BeanUtils.copyProperties(permission, dto);
                        return dto;
                    })
                    .toList();
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    permissionDtoList,
                    "Permissions listed successfully"
            );
        } catch (Exception e) {
            logger.error("Error when fetching permissions list entities: ", e);
            return new Response<>(false, ResponseCode.FAILURE, Collections.emptyList(),"Error when fetching permissions list");

        }
    }
}
