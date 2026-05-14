package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UnitSectionDto {
    private UUID uid;
    private String name;
    private UUID unitUid;
    private String unitName;
    private String waterMeter;
    private String gasMeter;
    private String squareMeters;
    private String electricityMeter;
    private BigDecimal price;
    private UUID currencyUid;
    private Boolean available;
    private List<RoomDto> roomDtoList;
}
