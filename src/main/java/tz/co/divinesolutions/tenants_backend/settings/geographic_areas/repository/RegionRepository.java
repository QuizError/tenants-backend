package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Region;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findFirstByNapaId(String napaId);
    Optional<Region> findFirstByUid(UUID uid);
}
