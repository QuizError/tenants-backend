package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegionData {
    private Long id;
    private String name;
    private String postcode;
}
