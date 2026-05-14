package tz.co.divinesolutions.tenants_backend.settings.currency.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.settings.currency.dto.CurrencyDto;
import tz.co.divinesolutions.tenants_backend.settings.currency.service.CurrencyService;

import java.util.UUID;

@RestController
@RequestMapping("currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CREATE_CURRENCY','ROLE_EDIT_CURRENCY')")
    public Response<CurrencyDto> getGeographicalAreaByType(@RequestBody CurrencyDto dto){
        return currencyService.save(dto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_CURRENCIES')")
    public Response<CurrencyDto> listAllCountries(){
        return currencyService.listActiveCurrencies();
    }

    @GetMapping("{uid}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_CURRENCY')")
    public Response<CurrencyDto> getGeographicalAreaByTypeAndParentUid(@PathVariable UUID uid){
        return currencyService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_CURRENCY')")
    public Response<CurrencyDto> delete(@PathVariable UUID uid){
        return currencyService.delete(uid);
    }
}
