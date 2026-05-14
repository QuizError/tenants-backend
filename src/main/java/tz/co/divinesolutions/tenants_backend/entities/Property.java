package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyFunctionStatus;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property extends BaseEntity {

    private String name;
    private String senderName;
    private Long ownerId;
    private Long agentId;
    private LocalDate startFunction;
    private LocalDate endFunction;
    private String contactPersonMobile;
    private String contactPersonEmail;

    private Boolean hasServiceCharge = false;
    private Boolean hasSecureDeposit = false;
    private Boolean notifyMeEndOfContract = false;
    private BigDecimal serviceChargeAmount;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private String serviceChargeDescription;

    @Enumerated(EnumType.STRING)
    private PropertyFunctionStatus functionStatus;

    @Enumerated(EnumType.STRING)
    private PropertyOwnershipType ownershipType;

    private Long regionId;
    private Long districtId;
    private Long wardId;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;
}
