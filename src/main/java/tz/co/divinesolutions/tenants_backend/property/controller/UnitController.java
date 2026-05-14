package tz.co.divinesolutions.tenants_backend.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.Unit;
import tz.co.divinesolutions.tenants_backend.property.dto.UnitDto;
import tz.co.divinesolutions.tenants_backend.property.service.UnitService;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("units")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @PostMapping
    public Response<Unit> save(@RequestBody UnitDto dto){
        return unitService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<Unit> findByUid(@PathVariable UUID uid){
        return unitService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Unit> delete(@PathVariable UUID uid){
        return unitService.delete(uid);
    }

    @GetMapping
    public List<Unit> units(){
        return unitService.units();
    }

    @GetMapping("/property/{uid}")
    public List<UnitDto> propertyUnits(@PathVariable UUID uid){
        return unitService.propertyUnits(uid);
    }
}
