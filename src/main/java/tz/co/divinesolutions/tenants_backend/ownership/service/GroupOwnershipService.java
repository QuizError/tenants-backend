package tz.co.divinesolutions.tenants_backend.ownership.service;

import org.springframework.data.domain.Page;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.globals.PageableParam;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupOwnershipService {
    Response<GroupOwnershipDto> save(GroupOwnershipDto dto);

    Optional<GroupOwnership> getOptionalByUid(UUID uid);

    Response<GroupOwnershipDto> findByUid(UUID uid);

    Response<GroupOwnershipDto> delete(UUID uid);

    Page<GroupOwnershipDto> searchGroupOwnerships(PageableParam pageableParam);

    List<GroupOwnership> groupOwnerships();
}
