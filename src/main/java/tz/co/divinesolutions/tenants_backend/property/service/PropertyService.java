package tz.co.divinesolutions.tenants_backend.property.service;

import tz.co.divinesolutions.tenants_backend.entities.Property;
import tz.co.divinesolutions.tenants_backend.property.dto.BroadcastDto;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyDto;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyService {
    Response<Property> save(PropertyDto dto);

    Optional<Property> getOptionalByUid(UUID uid);
    Optional<Property> getOptionalById(Long id);

    List<Property> properties();

    Response<Property> findByUid(UUID uid);

    Response<Property> delete(UUID uid);

    Response<Property> getMyProperties();

    Response<SMSDto> sendBroadcastSms(BroadcastDto dto);
}
