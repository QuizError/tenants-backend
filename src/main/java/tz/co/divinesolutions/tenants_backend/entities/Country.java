package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class Country extends BaseEntity{

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String nationality;

    @Column(length = 5)
    private String countryCode;

    @Column(length = 5)
    private String currency;
}
