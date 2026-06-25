package tz.co.divinesolutions.tenants_backend.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.property.dto.BroadcastDto;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyData;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyDto;
import tz.co.divinesolutions.tenants_backend.property.service.PropertyService;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;

import java.util.UUID;

@RestController
@RequestMapping("properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Response<PropertyData> save(@RequestBody PropertyDto dto){
        return propertyService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<PropertyData> save(@PathVariable UUID uid){
        return propertyService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<PropertyData> delete(@PathVariable UUID uid){
        return propertyService.delete(uid);
    }

    @GetMapping
    public Response<PropertyData> userList(){
        return propertyService.properties();
    }

    @GetMapping("/user")
    public Response<PropertyData> myProperties(){
        return propertyService.getMyProperties();
    }

    @PostMapping("/broadcast")
    public Response<SMSDto> save(@RequestBody BroadcastDto dto){
        return propertyService.sendBroadcastSms(dto);
    }
}
