package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Ward;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long>  {
    Optional<Ward> findFirstByUid(UUID uid);
    Optional<Ward> findFirstByNapaId(String napaId);
}
