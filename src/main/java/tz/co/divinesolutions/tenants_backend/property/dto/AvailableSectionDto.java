package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AvailableSectionDto {
    private UUID uid;
    private String name;
    private String location;
    private String unitName;
    private BigDecimal price;
    private UUID currencyUid;
    private String propertyName;
    private Boolean availability;
}
