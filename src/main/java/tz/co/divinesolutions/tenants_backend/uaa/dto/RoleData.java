package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class RoleData {
    private UUID uid;
    private String name;
    private String displayName;
    private String description;
    private List<PermissionDto> permissions;
}
