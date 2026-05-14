package tz.co.divinesolutions.tenants_backend.settings.currency.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyDto {
    private UUID uid;
    private String code;
    private String name;
}
