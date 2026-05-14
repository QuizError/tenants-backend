package tz.co.divinesolutions.tenants_backend.settings.currency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.Currency;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.settings.currency.dto.CurrencyDto;
import tz.co.divinesolutions.tenants_backend.settings.currency.repository.CurrencyRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final LoggedUser loggedUser;

    @Override
    public Response<CurrencyDto> save(CurrencyDto dto){
        try {
            Optional<Currency> optionalCurrency =  getOptionalByUid(dto.getUid());
            if (dto.getUid() != null && optionalCurrency.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Currency could not be found or may have been deleted from the system",
                        null
                );
            }
            Currency currency = optionalCurrency.orElse(new Currency());
            currency.setCode(dto.getCode());
            currency.setName(dto.getName());
            Currency saved =  currencyRepository.save(currency);
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    dto.getCode()+ " saved successfully",
                    convertToDto(saved)
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(false,
                    ResponseCode.DATABASE_ERROR,
                    Collections.emptyList(),
                    "Error when saving currency kindly contact support");
        }
    }

    @Override
    public Response<CurrencyDto> findByUid(UUID uid){
        try {
            Optional<Currency> optionalCurrency =  getOptionalByUid(uid);
            return optionalCurrency.map(currency -> new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success",
                    convertToDto(currency)
            )).orElseGet(() -> new Response<>(
                    false,
                    ResponseCode.NO_RECORD_FOUND,
                    "Currency could not be found or may have been deleted from the system",
                    null
            ));
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(false,
                    ResponseCode.DATABASE_ERROR,
                    Collections.emptyList(),
                    "Error when fetching currency kindly contact support");
        }
    }

    @Override
    public Response<CurrencyDto> listActiveCurrencies(){
        try {
            List<Currency> currencies =  currencyRepository.findAllByActiveTrue();
            if (currencies.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        Collections.emptyList(),
                        "No active currencies saved in the system at the moment"
                );
            }
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    currencies.stream()
                            .map(this::convertToDto)
                            .collect(Collectors.toList()),
                    "Success"
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(false,
                    ResponseCode.DATABASE_ERROR,
                    Collections.emptyList(),
                    "Error when saving currency kindly contact support");
        }
    }

    @Override
    public Optional<Currency> getOptionalByUid(UUID uid){
        return uid != null ? currencyRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<CurrencyDto> delete(UUID uid){
        try {
            if (loggedUser.getCurrentUser() != null && !loggedUser.isSuperAdmin()){
                return new Response<>(
                        false,
                        ResponseCode.UNAUTHORIZED,
                        "You are not authorized to delete currency",
                        null
                );
            }
            Optional<Currency> optionalCurrency = getOptionalByUid(uid);
            if (optionalCurrency.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Currency could not be found or may have been deleted from the system",
                        null
                );
            }

            Currency currency = optionalCurrency.get();

            currency.setDeleted(true);
            currency.setActive(false);
            currency.setDeletedAt(LocalDateTime.now());
            currency.setDeletedBy(loggedUser.getCurrentUserId());
            Currency saved = currencyRepository.save(currency);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    saved.getCode()+ " deleted successfully",
                    convertToDto(saved)
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    " Error when deleting currency",
                    null
            );
        }
    }

    private CurrencyDto convertToDto(Currency currency) {
        CurrencyDto dto = new CurrencyDto();
        BeanUtils.copyProperties(currency, dto);
        return dto;
    }
}
