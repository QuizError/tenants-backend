package tz.co.divinesolutions.tenants_backend.ownership.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GroupOwnershipDto {
    private UUID uid;
    private String name;
    private PropertyOwnershipType ownershipType;
}
