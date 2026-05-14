package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uid = UUID.randomUUID();

    private boolean active =  true;
    private boolean deleted = false;

    private LocalDateTime createdAt =  LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;
}
