package tz.co.divinesolutions.tenants_backend.ownership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnershipMember;
import tz.co.divinesolutions.tenants_backend.entities.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupOwnershipMemberRepository extends JpaRepository<GroupOwnershipMember,Long> {
    Optional<GroupOwnershipMember> findFirstByUid(UUID uid);
    List<GroupOwnershipMember> findAllByGroup(GroupOwnership groupOwnership);
    List<GroupOwnershipMember> findAllByUser(UserAccount user);
    @Query("SELECT DISTINCT gm.group.id FROM GroupOwnershipMember gm WHERE gm.user = :user")
    List<Long> findGroupIdsByUser(@Param("user") UserAccount user);
}
