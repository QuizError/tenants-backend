package tz.co.divinesolutions.tenants_backend.property.service;

import tz.co.divinesolutions.tenants_backend.entities.UnitSection;
import tz.co.divinesolutions.tenants_backend.property.dto.AvailableSectionDto;
import tz.co.divinesolutions.tenants_backend.property.dto.UnitSectionDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitSectionService {
    Response<UnitSection> save(UnitSectionDto dto);

    Optional<UnitSection> getOptionalByUid(UUID uid);

    Response<UnitSection> getSectionByUid(UUID uid);

    Response<UnitSection> deleteSection(UUID uid);

    List<UnitSectionDto> listAllSections();

    List<UnitSectionDto> listAllSectionsByUnitUid(UUID unitUid);

    List<AvailableSectionDto> myAvailableUnitSections(UUID userUid);

    void changeAvailability(UnitSection unitSection);
}
