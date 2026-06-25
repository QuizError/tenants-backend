package tz.co.divinesolutions.tenants_backend.settings.geographic_areas.service;

import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.District;
import tz.co.divinesolutions.tenants_backend.entities.Region;
import tz.co.divinesolutions.tenants_backend.entities.Village;
import tz.co.divinesolutions.tenants_backend.entities.Ward;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.AreaData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.dto.ConvertibleToAreaData;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.DistrictRepository;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.RegionRepository;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.VillageRepository;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.WardRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class GeographicAreasServiceImpl implements GeographicAreasService{

    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final VillageRepository villageRepository;

    private final RestTemplate restTemplate;

    @Value("${napa.base.url}")
    private String napaBaseUrl;

    @Value("${napa.header.key}")
    private String napaHeaderKey;

    @Value("${napa.header.value}")
    private String napaHeaderValue;

    @Override
    public void seedTanzaniaRegions(){
        if (regionRepository.findAll().isEmpty()){
            JsonNode regionData = getNapaData(napaBaseUrl+"/regions");
            JsonNode data = regionData.path("data");
            for (JsonNode item : data) {
                Optional<Region> optionalRegion = regionRepository.findFirstByNapaId(item.path("id").asText());
                Region region = optionalRegion.orElse(new Region());
                region.setCreatedBy(1L);
                region.setName(item.path("name").asText());
                region.setNapaId(item.path("id").asText());
                region.setPostcode(item.path("postcode").asText());
                regionRepository.save(region);
            }
        }
    }

    @Override
    public Response<AreaData> listRegions(){
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                regionRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()),
                "Success"
        );
    }

    @Override
    public Response<AreaData> listDistricts(UUID uid){
        Optional<Region> optionalRegion = regionRepository.findFirstByUid(uid);
        return optionalRegion.map(region -> new Response<>(
                true,
                ResponseCode.SUCCESS,
                districtRepository.findAllByRegionAndActiveTrue(region).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                "Success"
        )).orElseGet(() -> new Response<>(
                false,
                ResponseCode.NO_RECORD_FOUND,
                Collections.emptyList(),
                "Region could not be found or may have been deleted from the system"
        ));
    }

    @Override
    public Response<AreaData> listWards(UUID uid){
        Optional<District> optionalDistrict = districtRepository.findFirstByUid(uid);
        return optionalDistrict.map(district -> new Response<>(
                true,
                ResponseCode.SUCCESS,
                wardRepository.findAllByDistrictAndActiveTrue(district).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                "Success"
        )).orElseGet(() -> new Response<>(
                false,
                ResponseCode.NO_RECORD_FOUND,
                Collections.emptyList(),
                "District could not be found or may have been deleted from the system"
        ));
    }

    @Override
    public Response<AreaData> listVillages(UUID uid){
        Optional<Ward> optionalWard = wardRepository.findFirstByUid(uid);
        return optionalWard.map(ward -> new Response<>(
                true,
                ResponseCode.SUCCESS,
                villageRepository.findAllByWardAndActiveTrue(ward).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                "Success"
        )).orElseGet(() -> new Response<>(
                false,
                ResponseCode.NO_RECORD_FOUND,
                Collections.emptyList(),
                "Ward could not be found or may have been deleted from the system"
        ));
    }

    @Override
    @Transactional
    public Response<AreaData> seedRegionDistrict(UUID uid){
        List<District> districts = new ArrayList<>();

        Optional<Region> optionalRegion = regionRepository.findFirstByUid(uid);
        if (optionalRegion.isEmpty()){
            return new Response<>(
                    true,
                    ResponseCode.NO_RECORD_FOUND,
                    Collections.emptyList(),
                    "Region could not be found or may have been deleted from the system."
            );
        }
        Region region = optionalRegion.get();

        JsonNode districtData = getNapaData(napaBaseUrl+"/districts/"+region.getNapaId());
        JsonNode data = districtData.path("data");
        for (JsonNode item : data) {
            Optional<District> optionalDistrict = districtRepository.findFirstByNapaId(item.path("id").asText());
            District district = optionalDistrict.orElse(new District());
            district.setCreatedBy(1L);
            district.setRegion(optionalRegion.get());
            district.setName(item.path("name").asText());
            district.setNapaId(item.path("id").asText());
            district.setPostcode(item.path("postcode").asText());
            districts.add(districtRepository.save(district));
        }
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                districts.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                districts.size()+" districts for "+optionalRegion.get().getName()+" seeded successfully"
        );
    }

    @Transactional
    @Override
    public Response<AreaData> seedDistrictWards(UUID uid){
        List<Ward> wards = new ArrayList<>();

        Optional<District> optionalDistrict = districtRepository.findFirstByUid(uid);
        if (optionalDistrict.isEmpty()){
            return new Response<>(
                    true,
                    ResponseCode.NO_RECORD_FOUND,
                    Collections.emptyList(),
                    "District could not be found or may have been deleted from the system."
            );
        }
        District district = optionalDistrict.get();

        JsonNode wardData = getNapaData(napaBaseUrl+"/skip_councils/"+district.getNapaId());
        JsonNode data = wardData.path("data");
        for (JsonNode item : data) {
            Optional<Ward> optionalWard = wardRepository.findFirstByNapaId(item.path("id").asText());
            Ward ward = optionalWard.orElse(new Ward());
            ward.setCreatedBy(1L);
            ward.setDistrict(optionalDistrict.get());
            ward.setName(item.path("name").asText());
            ward.setNapaId(item.path("id").asText());
            ward.setPostcode(item.path("postcode").asText());
            wards.add(wardRepository.save(ward));
        }
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                wards.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                wards.size()+" wards for "+ optionalDistrict.get().getName()+" seeded successfully"
        );
    }

    @Transactional
    @Override
    public Response<AreaData> seedWardVillagesAndStreets(UUID uid){
        List<Village> villages = new ArrayList<>();

        Optional<Ward> optionalWard = wardRepository.findFirstByUid(uid);
        if (optionalWard.isEmpty()){
            return new Response<>(
                    true,
                    ResponseCode.NO_RECORD_FOUND,
                    Collections.emptyList(),
                    "Ward could not be found or may have been deleted from the system."
            );
        }
        Ward ward = optionalWard.get();

        JsonNode wardData = getNapaData(napaBaseUrl+"/wards/"+ward.getNapaId());
        JsonNode data = wardData.path("data");
        for (JsonNode item : data) {
            Optional<Village> optionalVillage = villageRepository.findFirstByNapaId(item.path("id").asText());
            Village village = optionalVillage.orElse(new Village());
            village.setCreatedBy(1L);
            village.setWard(optionalWard.get());
            village.setName(item.path("name").asText());
            village.setNapaId(item.path("id").asText());
            village.setPostcode(item.path("postcode").asText());
            villages.add(villageRepository.save(village));
        }
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                villages.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                villages.size()+" villages/streets for "+ optionalWard.get().getName()+" seeded successfully"
        );
    }

    public JsonNode getNapaData(String url) {
        log.info("******** Fetching data from NAPA: {}", url);

        HttpHeaders headers = createNapaHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("=====> Data from NAPA: {}", response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            log.error("Error fetching from NAPA endpoint: {}", url, e);
            throw new RuntimeException("Failed to fetch data from NAPA: " + url, e);
        }
    }

    private HttpHeaders createNapaHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(napaHeaderKey, napaHeaderValue);
        return headers;
    }

    private <T extends ConvertibleToAreaData> AreaData convertToDto(T entity) {
        AreaData dto = new AreaData();
        BeanUtils.copyProperties(entity, dto);
        dto.setParent(entity.getParentName());
        dto.setUid(entity.getUid());
        return dto;
    }

}
