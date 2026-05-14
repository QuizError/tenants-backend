package tz.co.divinesolutions.tenants_backend.property.service;

import tz.co.divinesolutions.tenants_backend.entities.UnitSectionDefinition;

import java.util.Optional;
import java.util.UUID;

public interface UnitSectionDefinitionService {
    Optional<UnitSectionDefinition> getOptionalByUid(UUID uid);
}
