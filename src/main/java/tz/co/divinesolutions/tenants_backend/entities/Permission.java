package tz.co.divinesolutions.tenants_backend.entities;

import tz.co.divinesolutions.tenants_backend.enums.PermissionScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity{

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    private String displayName;

    @Column(length = 25)
    private String permissionGroup;

    @Enumerated(EnumType.STRING)
    private PermissionScope scope;

    public Permission(String name, String displayName, String permissionGroup, PermissionScope scope) {
        this.name = name;
        this.displayName = displayName;
        this.permissionGroup = permissionGroup;
        this.scope = scope;
    }
}
