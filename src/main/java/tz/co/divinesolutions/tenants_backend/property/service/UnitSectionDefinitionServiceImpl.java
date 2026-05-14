package tz.co.divinesolutions.tenants_backend.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.UnitSectionDefinition;
import tz.co.divinesolutions.tenants_backend.property.repository.UnitSectionDefinitionRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitSectionDefinitionServiceImpl implements UnitSectionDefinitionService{

    private final UnitSectionDefinitionRepository unitSectionDefinitionRepository;

    @Override
    public Optional<UnitSectionDefinition> getOptionalByUid(UUID uid){
        return uid != null ? unitSectionDefinitionRepository.findFirstByUid(uid) : Optional.empty();
    }
}
