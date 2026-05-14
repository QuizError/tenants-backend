package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unit_sections")
public class UnitSection extends BaseEntity {

    private String name;
    private BigDecimal price;
    private Boolean available = false;
    private Long propertyId;
    private String electricityMeter;
    private String waterMeter;
    private String gasMeter;
    private String squareMeters;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @ManyToOne
    private Unit unit;

    @OneToMany(mappedBy = "unitSection")
    private List<UnitSectionDefinition> definitions;
}
