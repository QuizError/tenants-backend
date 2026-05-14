package tz.co.divinesolutions.tenants_backend.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.UnitSection;
import tz.co.divinesolutions.tenants_backend.property.dto.AvailableSectionDto;
import tz.co.divinesolutions.tenants_backend.property.dto.UnitSectionDto;
import tz.co.divinesolutions.tenants_backend.property.service.UnitSectionService;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("unit-sections")
public class UnitSectionController {

    @Autowired
    private UnitSectionService unitSectionService;

    @PostMapping
    public Response<UnitSection> save(@RequestBody UnitSectionDto dto){
        return unitSectionService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<UnitSection> getSectionByUid(@PathVariable UUID uid){
        return unitSectionService.getSectionByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<UnitSection> delete(@PathVariable UUID uid){
        return unitSectionService.deleteSection(uid);
    }

    @GetMapping
    public List<UnitSectionDto> units(){
        return unitSectionService.listAllSections();
    }

    @GetMapping("/unit/{uid}")
    public List<UnitSectionDto> listAllSectionsByUnitUid(@PathVariable UUID uid){
        return unitSectionService.listAllSectionsByUnitUid(uid);
    }

    @GetMapping("/available-units/{uid}")
    public List<AvailableSectionDto> myAvailableUnitSections(@PathVariable UUID uid){
        return unitSectionService.myAvailableUnitSections(uid);
    }

}
