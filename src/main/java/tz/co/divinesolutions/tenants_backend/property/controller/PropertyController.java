package tz.co.divinesolutions.tenants_backend.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.Property;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.property.dto.BroadcastDto;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyDto;
import tz.co.divinesolutions.tenants_backend.property.service.PropertyService;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Response<Property> save(@RequestBody PropertyDto dto){
        return propertyService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<Property> save(@PathVariable UUID uid){
        return propertyService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Property> delete(@PathVariable UUID uid){
        return propertyService.delete(uid);
    }

    @GetMapping
    public List<Property> userList(){
        return propertyService.properties();
    }

    @GetMapping("/user/{uid}")
    public Response<Property> myProperties(@PathVariable UUID uid){
        return propertyService.getMyProperties();
    }

    @PostMapping("/broadcast")
    public Response<SMSDto> save(@RequestBody BroadcastDto dto){
        return propertyService.sendBroadcastSms(dto);
    }
}
