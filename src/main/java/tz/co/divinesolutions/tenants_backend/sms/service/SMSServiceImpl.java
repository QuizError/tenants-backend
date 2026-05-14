package tz.co.divinesolutions.tenants_backend.sms.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;
import tz.co.divinesolutions.tenants_backend.entities.SMSCallBackHistory;
import tz.co.divinesolutions.tenants_backend.sms.dto.BeemSMSCallback;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSRequest;
import tz.co.divinesolutions.tenants_backend.sms.dto.SentSmsBody;
import tz.co.divinesolutions.tenants_backend.sms.repository.BeemCallBackRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService{

    private static final Logger log = LoggerFactory.getLogger(SMSServiceImpl.class);

    @Value("${app.sms.api.secret}")
    private String smsApiSecret;

    @Value("${app.sms.api.key}")
    private String smsApiKey;

    @Value("${app.sms.api.url}")
    private String smsApiUrl;

    private final RestTemplate restTemplate;

    private final BeemCallBackRepository beemCallBackRepository;

    @Override
    public String sendSms(SMSDto smsDto){
        SMSRequest request = new SMSRequest();
        request.setSource_addr(smsDto.getSourceAddr());
        request.setEncoding("0");
        request.setSchedule_time("");
        request.setRecipients(smsDto.getRecipients());
        request.setMessage(smsDto.getMessage());
        String response = sendSmsApi(request);

        log.info("SMS Request {}", request);
        log.info("SMS sending response: {}", response);
        return response;
    }

    public String sendSmsApi(SMSRequest smsRequest) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    smsApiUrl, HttpMethod.POST, authenticate(smsRequest), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("SMS sent successfully: " + response.getBody());
                ObjectMapper objectMapper = new ObjectMapper();
                BeemSMSCallback callback = objectMapper.readValue(response.getBody(), BeemSMSCallback.class);
                log.info("{}",callback);
//                return "success";
                return response.getBody();
            } else {
                log.error("Failed to send SMS: " + response.getStatusCode());
                return "Failed";
            }
        } catch (Exception e) {
            log.error("Error sending SMS", e);
            return "Error Sending Messages: "+e.getMessage();
        }
    }

    public <T> HttpEntity<T> authenticate(T data) {
        // Encode credentials
        String credentials = smsApiKey + ":" + smsApiSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + base64Credentials;

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);

        // Create the request entity with the actual data
        return new HttpEntity<>(data, headers);
    }

    @Override
    public void saveSentSms(SentSmsBody body){
        SMSCallBackHistory sms = new SMSCallBackHistory();
        sms.setSmsType(body.getSmsType());
        sms.setMessage(body.getMessage());
        sms.setCode(body.getCode());
        sms.setDuplicates(body.getDuplicates());
        sms.setClientId(body.getClientId());
        sms.setPropertyId(body.getPropertyId());
        sms.setRentalId(body.getRentalId());
        sms.setSuccessful(body.isSuccessful());
        sms.setValid(body.getValid());
        sms.setInvalid(body.getInvalid());
        sms.setRequestId(body.getRequestId());
        sms.setReceivedAt(LocalDateTime.now());
        sms.setSentTo(body.getSentTo());
        beemCallBackRepository.save(sms);
    }

}