package tz.co.divinesolutions.tenants_backend.ownership.service;

import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnershipMember;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupMembershipData;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipMemberDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

public interface GroupOwnershipMemberService {
    Response<GroupMembershipData> save(GroupOwnershipMemberDto dto);

    Response<GroupOwnershipMember> findByUid(UUID uid);

    Response<GroupOwnershipMember> delete(UUID uid);

    Response<GroupOwnershipMember> groupOwnershipMembers();

    Response<GroupOwnershipMember> listGroupMembers(UUID groupUid);

    Response<GroupOwnership> listMyGroups(UUID userUid);
}
