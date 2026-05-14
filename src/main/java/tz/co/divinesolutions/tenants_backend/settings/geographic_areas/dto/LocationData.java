package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Data;
import tz.co.divinesolutions.tenants_backend.enums.GeographicalAreaType;

import java.util.UUID;

@Data
public class LocationData {
    private UUID uid;
    private String name;
    private GeographicalAreaType geographicalAreaType;
}
