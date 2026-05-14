package tz.co.divinesolutions.tenants_backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.Rooms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unit_section_definitions")
public class UnitSectionDefinition extends BaseEntity {

    private Integer count;

    @Enumerated(EnumType.STRING)
    public Rooms room;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "unit_section_id")
    private UnitSection unitSection;
}
