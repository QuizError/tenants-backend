package tz.co.divinesolutions.tenants_backend.property.service;

import tz.co.divinesolutions.tenants_backend.entities.PropertyUnit;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyUnitDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

public interface PropertyUnitService {

    Response<PropertyUnit> save(PropertyUnitDto dto);

    List<PropertyUnit> propertyUnits();

    Response<PropertyUnit> findByUid(UUID uid);

    Response<PropertyUnit> delete(UUID uid);
}
