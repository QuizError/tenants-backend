package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto;

import java.util.UUID;

public interface ConvertibleToAreaData {
    String getParentName();
    UUID getUid();
}