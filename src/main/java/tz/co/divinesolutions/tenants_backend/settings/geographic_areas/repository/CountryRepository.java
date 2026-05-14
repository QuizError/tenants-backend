package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository;

import tz.co.divinesolutions.tenants_backend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    Optional<Country> findFirstByUid(UUID uuid);
    Optional<Country> findFirstByName(String name);
    List<Country> findAllByActiveTrue();
}
