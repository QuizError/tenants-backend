package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AreaData {
    private String name;
    private String postcode;
    private String napaId;
    private String parent;
}
