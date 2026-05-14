package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BroadcastDto {
    private String message;
    private UUID propertyUid;
}
