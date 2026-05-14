package tz.co.divinesolutions.tenants_backend.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Unit;
import tz.co.divinesolutions.tenants_backend.entities.UnitSection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitSectionRepository extends JpaRepository<UnitSection,Long> {
    Optional<UnitSection> findFirstByUid(UUID uid);
    List<UnitSection> findAllByUnit(Unit unit);
    List<UnitSection> findAllByAvailableTrueAndPropertyIdIn(List<Long> propertyIds);
}
