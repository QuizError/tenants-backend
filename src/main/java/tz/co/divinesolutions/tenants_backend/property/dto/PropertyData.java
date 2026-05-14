package tz.co.divinesolutions.tenants_backend.property.dto;

import lombok.Data;
import tz.co.divinesolutions.tenants_backend.enums.PropertyFunctionStatus;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PropertyData {
    private UUID uid;
    private String name;
    private String senderName;
    private String ownerGroup;
    private String agentName;
    private LocalDate startFunction;
    private LocalDate endFunction;
    private String location;
    private String contactPersonMobile;
    private String contactPersonEmail;
    private Boolean hasServiceCharge;
    private Boolean hasSecureDeposit;
    private Boolean notifyMeEndOfContract;
    private BigDecimal serviceChargeAmount;
    private String currency;
    private String serviceChargeDescription;
    private PropertyFunctionStatus functionStatus;
    private PropertyOwnershipType ownershipType;
}
