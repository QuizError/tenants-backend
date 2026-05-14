package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import tz.co.divinesolutions.tenants_backend.enums.GeographicalAreaType;
import lombok.Data;

import java.util.UUID;

@Data
public class GeographicalAreaDto {
    private UUID parentUid;
    private GeographicalAreaType geographicalAreaType;
}
