package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service;

import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.CountryData;

import java.io.IOException;

public interface CountryService {
    Response<CountryData> listAllCountries();

    void seed() throws IOException;
}
