package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegionResponse {
    private List<RegionData> data;
}
