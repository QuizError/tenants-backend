package tz.co.divinesolutions.tenants_backend.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.PropertyUnit;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyUnitRepository extends JpaRepository<PropertyUnit,Long> {
    Optional<PropertyUnit> findFirstByUid(UUID uid);
}
