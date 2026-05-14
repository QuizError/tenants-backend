package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.OwnershipMemberStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_ownership_members")
public class GroupOwnershipMember extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private OwnershipMemberStatus memberStatus;

    @ManyToOne
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupOwnership group;

}
