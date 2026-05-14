package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.District;
import tz.co.divinesolutions.tenants_backend.entities.Region;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {
    Optional<District> findFirstByUid(UUID uid);
    Optional<District> findFirstByNapaId(String  napaId);
    List<District> findAllByRegionAndActiveTrue(Region region);
}
