package tz.co.divinesolutions.tenants_backend.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.SmsType;

@Getter
@Setter
@NoArgsConstructor
public class SentSmsBody {
        private SmsType smsType;
        private String message;
        private Integer code;
        private Integer duplicates;
        private Long clientId;
        private Long propertyId;
        private Long rentalId;
        private boolean successful;
        private Integer valid;
        private Integer invalid;
        private Long requestId;
        private String sentTo;
}
