package tz.co.divinesolutions.tenants_backend.property.service;

import tz.co.divinesolutions.tenants_backend.entities.Unit;
import tz.co.divinesolutions.tenants_backend.property.dto.UnitDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitService {
    Response<Unit> save(UnitDto dto);

    Optional<Unit> getOptionalByUid(UUID uid);

    Response<Unit> findByUid(UUID uid);

    Response<Unit> delete(UUID uid);

    List<Unit> units();

    List<UnitDto> propertyUnits(UUID propertyUid);
}
