package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.controller;

import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.CountryData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.AreaData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service.CountryService;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service.GeographicAreasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class GeographicAreasController {

    private final CountryService countryService;
    private final GeographicAreasService geographicAreasService;

    @GetMapping("/countries")
    public Response<CountryData> listAllCountries(){
        return countryService.listAllCountries();
    }

    @GetMapping("/seed-districts/{uid}")
    public Response<AreaData> seedRegionDistrict(@PathVariable UUID uid){
        try {
            return geographicAreasService.seedRegionDistrict(uid);
        }
        catch (Exception e){
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    Collections.emptyList(),
                    "An error has occurred when seeding district kindly contact support."
            );
        }
    }

    @GetMapping("/seed-wards/{uid}")
    public Response<AreaData> seedDistrictWards(@PathVariable UUID uid){
        try {
            return geographicAreasService.seedDistrictWards(uid);
        }
        catch (Exception e){
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    Collections.emptyList(),
                    "An error has occurred when seeding wards kindly contact support."
            );
        }
    }

    @GetMapping("/seed-villages/{uid}")
    public Response<AreaData> seedWardVillagesAndStreets(@PathVariable UUID uid){
        try {
            return geographicAreasService.seedWardVillagesAndStreets(uid);
        }
        catch (Exception e){
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    Collections.emptyList(),
                    "An error has occurred when seeding villages/streets kindly contact support."
            );
        }
    }

}
