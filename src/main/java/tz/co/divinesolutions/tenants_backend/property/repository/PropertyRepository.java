package tz.co.divinesolutions.tenants_backend.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Property;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property,Long> {

    Optional<Property> findFirstByUid(UUID uid);
    List<Property> findAllByOwnerIdAndOwnershipType(Long id, PropertyOwnershipType ownershipType);
    List<Property> findAllByOwnerIdInAndActiveTrue(List<Long> ids);
}
