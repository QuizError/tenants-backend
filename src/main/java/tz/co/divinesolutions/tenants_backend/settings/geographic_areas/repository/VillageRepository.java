package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Village;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VillageRepository extends JpaRepository<Village,Long> {
    Optional<Village> findFirstByUid(UUID uid);
    Optional<Village> findFirstByNapaId(String napaId);
}
