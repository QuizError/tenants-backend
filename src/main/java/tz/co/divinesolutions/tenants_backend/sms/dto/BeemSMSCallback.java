package tz.co.divinesolutions.tenants_backend.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeemSMSCallback {
    private boolean successful;
    private Long request_id;
    private Integer code;
    private String message;
    private Integer valid;
    private Integer invalid;
    private Integer duplicates;
}