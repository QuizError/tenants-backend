package tz.co.divinesolutions.tenants_backend.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForgotPasswordRequest {
    private String msisdn;
    private String firstname;
}
