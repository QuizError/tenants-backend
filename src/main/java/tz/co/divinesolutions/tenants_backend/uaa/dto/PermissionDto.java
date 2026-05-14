package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private UUID uid;
    private String name;
    private String permissionGroup;
    private String displayName;
}
