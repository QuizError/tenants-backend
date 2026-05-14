package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.PropertyType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "units")
public class Unit  extends BaseEntity {
    private String name;
    private String descriptions;

    @ManyToOne
    private Property property;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

}
