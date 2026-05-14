package tz.co.divinesolutions.tenants_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.SmsType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sms_callback_histories")
public class SMSCallBackHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime receivedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private SmsType smsType;

    private Long propertyId;
    private Long rentalId;
    private Long clientId;

    private boolean successful;
    private Long requestId;
    private Integer code;
    private Integer valid;
    private Integer invalid;
    private Integer duplicates;
    private String sentTo;
}