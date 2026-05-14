package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UnitDto {
    private UUID uid;
    private String name;
    private UUID propertyUid;
    private String descriptions;
    private PropertyType propertyType;
}
