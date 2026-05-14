package tz.co.divinesolutions.tenants_backend.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.UnitSection;
import tz.co.divinesolutions.tenants_backend.entities.UnitSectionDefinition;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitSectionDefinitionRepository extends JpaRepository<UnitSectionDefinition,Long> {
    Optional<UnitSectionDefinition> findFirstByUid(UUID uid);

    boolean existsByUnitSection(UnitSection unitSection);
    void  deleteAllByUnitSection(UnitSection unitSection);
}
