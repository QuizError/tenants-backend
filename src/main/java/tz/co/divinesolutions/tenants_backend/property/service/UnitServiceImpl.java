package tz.co.divinesolutions.tenants_backend.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.Property;
import tz.co.divinesolutions.tenants_backend.entities.Unit;
import tz.co.divinesolutions.tenants_backend.property.dto.UnitDto;
import tz.co.divinesolutions.tenants_backend.property.repository.UnitRepository;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{

    private final PropertyService propertyService;
    private final UnitRepository unitRepository;

    @Override
    public Response<Unit> save(UnitDto dto){
        try {

            Optional<Property> optionalProperty = propertyService.getOptionalByUid(dto.getPropertyUid());
            if (optionalProperty.isEmpty()){
                return new Response<>(false, ResponseCode.NO_RECORD_FOUND, "Property could not be found or may have been deleted from the system", null);
            }
            Property property = optionalProperty.get();

            Optional<Unit> optionalUnit = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.NO_RECORD_FOUND, "Unit could not be found or may have been deleted from the system", null);
            }

            Unit unit = optionalUnit.orElse(new Unit());
            unit.setProperty(property);
            unit.setName(dto.getName());
            unit.setPropertyType(dto.getPropertyType());
            unit.setDescriptions(dto.getDescriptions());
            Unit saved = unitRepository.save(unit);

            return new Response<>(true, ResponseCode.SUCCESS, "Unit saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT_DATA, "Error when saving unit", null);
        }
    }
    @Override
    public Optional<Unit> getOptionalByUid(UUID uid){
        return uid != null ? unitRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<Unit> findByUid(UUID uid){
        try {
            Optional<Unit> optionalUnit = getOptionalByUid(uid);
            return optionalUnit.map(unit -> new Response<>(true, ResponseCode.SUCCESS, "Success", unit)).orElseGet(() -> new Response<>(false, ResponseCode.NO_RECORD_FOUND, "Unit could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.FAILURE, "Error when fetching unit data", null);
        }
    }

    @Override
    public Response<Unit> delete(UUID uid){
        try {
            Optional<Unit> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE_RECORD, "Unit could not be found or may have been deleted from the system", null);
            }
            unitRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Unit deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.FAILURE, "Error when deleting unit data", null);
        }
    }

    @Override
    public List<Unit> units(){
        return unitRepository.findAll();
    }

    @Override
    public List<UnitDto> propertyUnits(UUID propertyUid){
        try {
            Optional<Property> optionalProperty = propertyService.getOptionalByUid(propertyUid);
            if (optionalProperty.isEmpty()){
                log.error("Property with uid: {} could not be found", propertyUid);
                return Collections.emptyList();
            }
            Property property = optionalProperty.get();
            List<UnitDto> unitDtoList = new ArrayList<>();
            for (Unit unit : unitRepository.findAllByProperty(property)) {
                UnitDto dto = new UnitDto();
                dto.setName(unit.getName());
                dto.setUid(unit.getUid());
                dto.setPropertyUid(unit.getProperty() != null ? unit.getProperty().getUid() : null);
                dto.setDescriptions(unit.getDescriptions());
                unitDtoList.add(dto);
            }

            return unitDtoList;
        }
        catch (Exception e){
            return Collections.emptyList();
        }
    }
}
