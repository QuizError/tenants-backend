package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "regions")
public class Region extends BaseEntity{
    private String name;
    private String postcode;
    private String napaId;
}
