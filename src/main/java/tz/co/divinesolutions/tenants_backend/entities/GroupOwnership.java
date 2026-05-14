package tz.co.divinesolutions.tenants_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_ownerships")
public class GroupOwnership extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private PropertyOwnershipType ownershipType;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<GroupOwnershipMember> members;
}
