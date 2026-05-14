package tz.co.divinesolutions.tenants_backend.ownership.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupMembershipData {
    private String groupName;
    private String memberName;
    private UUID uid;
}
