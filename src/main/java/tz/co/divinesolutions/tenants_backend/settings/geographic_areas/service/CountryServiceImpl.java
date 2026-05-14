package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service;

import org.springframework.beans.BeanUtils;
import tz.co.divinesolutions.tenants_backend.entities.Country;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.CountryData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.LocationData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{

    private final CountryRepository countryRepository;

    @Override
    public Response<CountryData> listAllCountries(){
        try {
            return new Response<>(true,
                    ResponseCode.SUCCESS,
                    countryRepository.findAllByActiveTrue().stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList()),
                    "Success");
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(false,
                    ResponseCode.DATABASE_ERROR,
                    Collections.emptyList(),
                    "Error when fetching country list");
        }
    }

    @Override
    public void seed() throws IOException {
        log.info("***** Start Seeding Countries ");
        List<Country> countries = new ArrayList<>();
        if (countryRepository.findAll().isEmpty()){
            ClassPathResource countryResource = new ClassPathResource("countries.xlsx");
            try (InputStream inputStream = countryResource.getInputStream()) {
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int firstRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();

                for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    Country country = new Country();
                    country.setCreatedBy(1L);
                    country.setName(getCellValueAsString(row.getCell(0)));
                    country.setCountryCode(getCellValueAsString(row.getCell(1)));
                    country.setCurrency(getCellValueAsString(row.getCell(2)));
                    country.setNationality(getCellValueAsString(row.getCell(3)));
                    countryRepository.save(country);
                    countries.add(country);
                }
                workbook.close();
                inputStream.close();
                log.info("Seeded {} countries", countries.size());
            }
        }
        else {
            log.info("***** countries already seeded *****");
        }
    }

    private String getCellValueAsString(Cell cell){
        if (cell == null){
            return "";
        }

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BLANK -> "";
            default -> "";
        };
    }

    private Integer getCellValueAsInteger(Cell cell){
        if (cell == null){
            return null;
        }

        switch (cell.getCellType()){
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    log.warn("Could not parse value '{}' to Integer", cell.getStringCellValue());
                    return null;
                }
            case BLANK:
                return null;

            default:
                return null;
        }
    }

    private CountryData convertToDto(Country country) {
        CountryData dto = new CountryData();
        BeanUtils.copyProperties(country, dto);
        return dto;
    }
}
