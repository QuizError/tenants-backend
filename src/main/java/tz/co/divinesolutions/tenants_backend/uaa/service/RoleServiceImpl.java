package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.entities.Role;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.uaa.dto.*;
import tz.co.divinesolutions.tenants_backend.uaa.repository.RoleRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final LoggedUser loggedUser;

    @Override
    public Response<Role> saveRole(RoleDto dto){
        try {
            if (loggedUser.getCurrentUser() == null){
                return new Response<>(false, ResponseCode.UNAUTHORIZED,"You need to login to proceed",null);
            }
            log.info("Saving role with DTO: {}",dto);
            Optional<Role> optionalRole = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && optionalRole.isEmpty()){
                return new Response<>(false,ResponseCode.NO_RECORD_FOUND,"Role could not be found or may have been deleted from the system",null);
            }
            Role role = optionalRole.orElse(new Role());
            role.setCreatedBy(loggedUser.getCurrentUserId());
            role.setDescription(dto.getDescription());
            role.setName(formatRoleName(dto.getDisplayName()));
            role.setDisplayName(dto.getDisplayName().toUpperCase());
            Role savedRole = roleRepository.save(role);
            return new Response<>(true, ResponseCode.SUCCESS,"Role saved successfully",savedRole);
        }
        catch (Exception e){
            log.info("{} when saving role",e.getMessage());
            return new Response<>(false, ResponseCode.FAILURE,"Error when saving role",null);
        }
    }

    @Override
    public Response<RoleData> findAllRoles(){

        List<Role> roles;

        if (loggedUser.isSuperAdmin()) {
            roles = roleRepository.findAllByActiveTrue();
        } else {
            roles = roleRepository.findAllByIsSystemRoleFalseAndActiveTrue();
        }

        List<RoleData> roleDataList = roles.stream()
                .map(role -> {
                    RoleData dto = new RoleData();
                    BeanUtils.copyProperties(role, dto);
                    return dto;
                })
                .toList();
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                roleDataList,
                "Roles listed successfully"
        );
    }

    @Override
    public Response<RoleData> findByUid(UUID uid){
        Optional<Role> optionalRole = getOptionalByUid(uid);
        if (optionalRole.isEmpty()){
            return new Response<>(false, ResponseCode.NO_RECORD_FOUND,"Role could not be found or may have been deleted from the system", null);
        }
        Role role = optionalRole.get();

        List<PermissionDto> permissionDtoList = role.getPermissions().stream()
                .map(permission -> {
                    PermissionDto permissionDto = new PermissionDto();
                    BeanUtils.copyProperties(permission, permissionDto);
                    return permissionDto;
                })
                .toList();


        RoleData dto = new RoleData();
        BeanUtils.copyProperties(role, dto);
        dto.setPermissions(permissionDtoList);

        return new Response<>(true, ResponseCode.SUCCESS,"Success", dto);
    }

    @Override
    public Optional<Role> getOptionalByUid(UUID uid){
        return uid != null ? roleRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Optional<Role> getOptionalByName(String name){
        return name != null && !name.isEmpty() ? roleRepository.findByName(name) : Optional.empty();
    }

    @Override
    public List<Role> findAllByUidIn(Set<UUID> roleUids) {
        return roleRepository.findAllByUidIn(roleUids);
    }

    @Override
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    //FORMATTING ROLE NAME
    public String formatRoleName(String input) {
        if (input == null) return null;

        return input.trim()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .toUpperCase();
    }

}
