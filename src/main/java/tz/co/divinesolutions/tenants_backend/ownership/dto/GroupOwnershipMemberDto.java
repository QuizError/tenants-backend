package tz.co.divinesolutions.tenants_backend.ownership.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.OwnershipMemberStatus;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GroupOwnershipMemberDto {
    private UUID uid;
    private UUID userUid;
    private UUID groupUid;
    private OwnershipMemberStatus memberStatus;
}
