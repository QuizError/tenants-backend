package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service;

import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tz.co.divinesolutions.tenants_backend.entities.District;
import tz.co.divinesolutions.tenants_backend.enums.GeographicalAreaType;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.AreaData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.GeographicalAreaDto;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.LocationData;

import java.util.UUID;

public interface GeographicAreasService {
    void seedTanzaniaRegions();

    Response<AreaData> listRegions();

    Response<AreaData> listDistricts(UUID uid);

    Response<AreaData> listWards(UUID uid);

    Response<AreaData> listVillages(UUID uid);

    Response<AreaData> seedRegionDistrict(UUID uid);

    @Transactional
    Response<AreaData> seedDistrictWards(UUID uid);

    @Transactional
    Response<AreaData> seedWardVillagesAndStreets(UUID uid);
}
