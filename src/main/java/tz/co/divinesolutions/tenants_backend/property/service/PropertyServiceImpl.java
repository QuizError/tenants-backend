package tz.co.divinesolutions.tenants_backend.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.*;
import tz.co.divinesolutions.tenants_backend.entities.Currency;
import tz.co.divinesolutions.tenants_backend.enums.PropertyFunctionStatus;
import tz.co.divinesolutions.tenants_backend.enums.PropertyOwnershipType;
import tz.co.divinesolutions.tenants_backend.ownership.repository.GroupOwnershipMemberRepository;
import tz.co.divinesolutions.tenants_backend.ownership.service.GroupOwnershipService;
import tz.co.divinesolutions.tenants_backend.property.dto.BroadcastDto;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyData;
import tz.co.divinesolutions.tenants_backend.property.dto.PropertyDto;
import tz.co.divinesolutions.tenants_backend.property.repository.PropertyRepository;
import tz.co.divinesolutions.tenants_backend.settings.currency.repository.CurrencyRepository;
import tz.co.divinesolutions.tenants_backend.settings.geographic_areas.repository.VillageRepository;
import tz.co.divinesolutions.tenants_backend.sms.dto.Recipient;
import tz.co.divinesolutions.tenants_backend.sms.dto.SMSDto;
import tz.co.divinesolutions.tenants_backend.sms.service.SMSService;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserAccountRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService{

    private final GroupOwnershipMemberRepository groupOwnershipMemberRepository;
    private final GroupOwnershipService groupOwnershipService;
    private final PropertyRepository propertyRepository;
    private final CurrencyRepository currencyRepository;
    private final VillageRepository villageRepository;
    private final SMSService smsService;
    private final UserAccountRepository userAccountRepository;
    private final LoggedUser loggedUser;

    @Override
    public Response<PropertyData> save(PropertyDto dto){
        try {

            Optional<Currency> optionalCurrency = currencyRepository.findFirstByUid(dto.getUid());
            if (optionalCurrency.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE_RECORD, "Currency could not be found or may have been deleted from the system", null);
            }
            Currency currency = optionalCurrency.get();

            Optional<Property> optionalProperty = getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && optionalProperty.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.DUPLICATE_RECORD,
                        "Property could not be found or may have been deleted from the system",
                        null
                );
            }

            Optional<Village> optionalVillage = villageRepository.findFirstByUid(dto.getStreetUid());
            if (optionalVillage.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.DUPLICATE_RECORD,
                        "Street/Village could not be found or may have been deleted from the system",
                        null
                );
            }
            Village village = optionalVillage.get();

            Property property = optionalProperty.orElse(new Property());

            if (dto.getOwnershipType().equals(PropertyOwnershipType.PRIVATE)){
                Optional<UserAccount> optionalUser = userAccountRepository.findFirstByUid(dto.getOwnerUid());
                Long ownerId = optionalUser.map(BaseEntity::getId).orElse(null);
                property.setOwnerId(ownerId);
            }else {
                Optional<GroupOwnership> optionalGroupOwnership = groupOwnershipService.getOptionalByUid(dto.getOwnerUid());
                Long ownerId = optionalGroupOwnership.map(BaseEntity::getId).orElse(null);
                property.setOwnerId(ownerId);
            }
            property.setName(dto.getName());
            property.setHasServiceCharge(dto.getHasServiceCharge());
            property.setServiceChargeAmount(dto.getServiceChargeAmount());
            property.setCurrency(currency);
            property.setRegionId(village.getWard().getDistrict().getRegion().getId());
            property.setDistrictId(village.getWard().getDistrict().getId());
            property.setWardId(village.getWard().getId());
            property.setVillage(village);
            property.setServiceChargeDescription(dto.getServiceChargeDescription());
            property.setContactPersonEmail(dto.getContactPersonEmail());
            property.setContactPersonMobile(dto.getContactPersonMobile());
            property.setNotifyMeEndOfContract(dto.getNotifyMeEndOfContract());
            property.setSenderName(dto.getSenderName());
            property.setStartFunction(LocalDate.now());
            property.setOwnershipType(dto.getOwnershipType());
            property.setFunctionStatus(PropertyFunctionStatus.ACTIVE);
            Property saved = propertyRepository.save(property);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Property saved successfully",
                    convertToDto(saved));
        }
        catch (Exception e){
            log.error("***** Error on saving property: {}",e.getMessage());
            return new Response<>(
                    false,
                    ResponseCode.INVALID_INPUT_DATA,
                    "Error when saving property",
                    null
            );
        }
    }
    @Override
    public Optional<Property> getOptionalByUid(UUID  uid){
        return uid != null ? propertyRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Optional<Property> getOptionalById(Long  id){
        return id != null  ? propertyRepository.findById(id) : Optional.empty();
    }
    @Override
    public Response<PropertyData> properties(){
        return new Response<>(
                true,
                ResponseCode.UNAUTHORIZED,
                propertyRepository.findAll().stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                "Success"
        );
    }

    @Override
    public Response<PropertyData> findByUid(UUID uid){
        try {
            Optional<Property> optionalProperty = getOptionalByUid(uid);
            return optionalProperty.map(property -> new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success", convertToDto(property)))
                    .orElseGet(() -> new Response<>(
                            false,
                            ResponseCode.NO_RECORD_FOUND,
                            "Property could not be found or may have been deleted from the system",
                            null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Property: {}",e.getMessage());
            return new Response<>(true, ResponseCode.FAILURE, "Error when fetching property data", null);
        }
    }

    @Override
    public Response<PropertyData> delete(UUID uid){
        try {
            Optional<Property> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE_RECORD, "Property could not be found or may have been deleted from the system", null);
            }
            propertyRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Property deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching property: {}",e.getMessage());
            return new Response<>(true, ResponseCode.FAILURE, "Error when deleting property data", null);
        }
    }

    @Override
    public Response<PropertyData> getMyProperties(){
        if (loggedUser.getCurrentUser() == null){
            return new Response<>(
                    true,
                    ResponseCode.UNAUTHORIZED,
                    Collections.emptyList(),
                    "This user is not authorized for this request"
            );

        }
        List<Long> myGroupsIds = groupOwnershipMemberRepository.findGroupIdsByUser(loggedUser.getCurrentUser());
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                propertyRepository.findAllByOwnerIdInAndActiveTrue(myGroupsIds).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()),
                "Success"
        );
    }

    @Override
    public Response<SMSDto> sendBroadcastSms(BroadcastDto dto){
        Optional<Property> optionalProperty = getOptionalByUid(dto.getPropertyUid());
        if (optionalProperty.isPresent()){
            //Prepare an empty list of Recipients
            List<Recipient> recipients = new ArrayList<>();

            Property property = optionalProperty.get();
            String senderName = property.getSenderName() != null && !property.getSenderName().isEmpty() ?  property.getSenderName() : "HOMES APP";
//            List<String> msisdnList = userAccountRepository.userMsisdnList(property.getId()); TO BE IMPLEMENTED
            List<String> msisdnList =  new ArrayList<>();

            for (int i = 0; i < msisdnList.size(); i++) {
                Recipient recipient = new Recipient();
                recipient.setRecipient_id(i + 1);
                recipient.setDest_addr(msisdnList.get(i));
                recipients.add(recipient);
            }

            SMSDto smsDto = new SMSDto();
            smsDto.setMessage(dto.getMessage());
            smsDto.setSourceAddr(senderName);
            smsDto.setRecipients(recipients);
            smsService.sendSms(smsDto);
            log.info("The send list is: {}",  smsDto);
            return new Response<>(true, ResponseCode.SUCCESS,"SMS request sent successfully", smsDto);
        }
        else {
            return new Response<>(false, ResponseCode.NO_RECORD_FOUND,Collections.emptyList(),"Property could not be found or may have been deleted from the system");
        }
    }

    private PropertyData convertToDto(Property property) {
        PropertyData dto = new PropertyData();
        BeanUtils.copyProperties(property, dto);
        dto.setLocation(property.getVillage().getName());
        dto.setCurrency(property.getCurrency().getCode());
        return dto;
    }
}
