package tz.co.divinesolutions.tenants_backend.ownership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupOwnershipRepository extends JpaRepository<GroupOwnership,Long>, JpaSpecificationExecutor<GroupOwnership> {
    Optional<GroupOwnership> findFirstByUid(UUID uid);
}
