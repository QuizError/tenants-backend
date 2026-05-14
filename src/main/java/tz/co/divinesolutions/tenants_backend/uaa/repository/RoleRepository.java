package tz.co.divinesolutions.tenants_backend.uaa.repository;

import tz.co.divinesolutions.tenants_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findFirstByUid(UUID uid);
    boolean existsByNameAndActiveTrue(String name);
    List<Role> findAllByUidIn(Set<UUID> uidList);
    List<Role> findAllByIsSystemRoleFalseAndActiveTrue();
    List<Role> findAllByActiveTrue();
}
