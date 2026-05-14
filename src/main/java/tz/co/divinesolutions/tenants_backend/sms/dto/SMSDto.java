package tz.co.divinesolutions.tenants_backend.sms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SMSDto {
    private String sourceAddr;
    private String message;
    private List<Recipient> recipients;

    @Override
    public String toString() {
        return "SMSDto{" +
                "sourceAddr='" + sourceAddr + '\'' +
                ", message='" + message + '\'' +
                ", recipients=" + recipients +
                '}';
    }
}
