package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CountryData {
    private Long id;
    private UUID uid;
    private String name;
    private String currency;
    private String nationality;
    private String countryCode;
}
