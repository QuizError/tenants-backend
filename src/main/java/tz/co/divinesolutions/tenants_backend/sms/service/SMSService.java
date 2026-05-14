package tz.co.divinesolutions.tenants_backend.sms.service;

import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;
import tz.co.divinesolutions.tenants_backend.sms.dto.SentSmsBody;

public interface SMSService {
    String sendSms(SMSDto smsDto);

    void saveSentSms(SentSmsBody body);
}
