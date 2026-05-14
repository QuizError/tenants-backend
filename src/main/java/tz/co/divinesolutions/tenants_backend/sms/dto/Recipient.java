package tz.co.divinesolutions.tenants_backend.sms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipient {
    private Integer recipient_id;
    protected String dest_addr;

    public Recipient(Integer recipient_id, String dest_addr) {
        this.recipient_id = recipient_id;
        this.dest_addr = dest_addr;
    }
}