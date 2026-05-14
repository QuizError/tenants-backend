package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.Rooms;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {
    private Integer count;
    private Rooms room;
}
