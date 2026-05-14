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
@Table(name = "villages")
public class Village extends BaseEntity implements ConvertibleToAreaData {
    private String name;
    private String postcode;
    private String napaId;

    @ManyToOne
    private Ward ward;

    @Override
    public String getParentName() {
        return this.ward.getName();
    }
}
