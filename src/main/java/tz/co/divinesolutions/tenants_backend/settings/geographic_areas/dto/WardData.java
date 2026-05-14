package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WardData {
    private String name;
    private String postcode;
    private Long id;
    private ParentDto parent;
}
