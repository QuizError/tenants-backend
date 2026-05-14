package tz.co.divinesolutions.tenants_backend.settings.currency.service;

import tz.co.divinesolutions.tenants_backend.entities.Currency;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.settings.currency.dto.CurrencyDto;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyService {
    Response<CurrencyDto> save(CurrencyDto dto);

    Response<CurrencyDto> findByUid(UUID uid);

    Response<CurrencyDto> listActiveCurrencies();

    Optional<Currency> getOptionalByUid(UUID uid);

    Response<CurrencyDto> delete(UUID uid);
}
