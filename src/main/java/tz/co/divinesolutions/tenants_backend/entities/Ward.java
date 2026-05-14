package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.ConvertibleToAreaData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wards")
public class Ward extends BaseEntity implements ConvertibleToAreaData {
    private String name;
    private String postcode;
    private String napaId;

    @ManyToOne
    private District district;

    @Override
    public String getParentName() {
        return this.district.getName();
    }
}
