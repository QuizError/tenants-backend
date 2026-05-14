package tz.co.divinesolutions.tenants_backend.uaa.repository;

import tz.co.divinesolutions.tenants_backend.entities.Permission;
import tz.co.divinesolutions.tenants_backend.enums.PermissionScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> , JpaSpecificationExecutor<Permission> {
    Optional<Permission> findByName(String name);
    Set<Permission> findAllByIdIn(Set<Long> ids);
    boolean existsByNameAndActiveTrue(String name);
    List<Permission> findAllByScopeAndActiveTrue(PermissionScope permissionScope);
    List<Permission> findAllByActiveTrue();
}