package tz.co.divinesolutions.tenants_backend.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Property;
import tz.co.divinesolutions.tenants_backend.entities.Unit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<Unit,Long> {
    Optional<Unit> findFirstByUid(UUID uid);
    List<Unit> findAllByProperty(Property property);
}
