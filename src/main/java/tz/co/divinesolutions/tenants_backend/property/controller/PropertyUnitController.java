package tz.co.divinesolutions.tenants_backend.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.PropertyUnit;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyUnitDto;
import tz.co.divinesolutions.tenants_backend.property.service.PropertyUnitService;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("property-units")
public class PropertyUnitController {

    @Autowired
    private PropertyUnitService propertyUnitService;

    @PostMapping
    public Response<PropertyUnit> save(@RequestBody PropertyUnitDto dto){
        return propertyUnitService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<PropertyUnit> save(@PathVariable UUID uid){
        return propertyUnitService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<PropertyUnit> delete(@PathVariable UUID uid){
        return propertyUnitService.delete(uid);
    }

    @GetMapping
    public List<PropertyUnit> propertyUnits(){
        return propertyUnitService.propertyUnits();
    }
}
