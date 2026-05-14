package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyFunctionStatus;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PropertyDto {
    private UUID uid;
    private String name;
    private Boolean hasServiceCharge = false;
    private Boolean notifyMeEndOfContract = false;
    private BigDecimal serviceChargeAmount;
    private UUID currencyUid;
    private String serviceChargeDescription;
    private String senderName;
    private UUID ownerUid;
    private String location;
    private LocalDate endFunction;
    private LocalDate startFunction;
    private PropertyOwnershipType ownershipType;
    private PropertyFunctionStatus functionStatus;
    private String contactPersonMobile;
    private String contactPersonEmail;
}
