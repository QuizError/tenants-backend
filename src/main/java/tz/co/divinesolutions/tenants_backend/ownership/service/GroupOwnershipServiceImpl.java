package tz.co.divinesolutions.tenants_backend.ownership.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.globals.*;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipDto;
import tz.co.divinesolutions.tenants_backend.ownership.repository.GroupOwnershipRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupOwnershipServiceImpl implements GroupOwnershipService {
    private static final Logger logger = LoggerFactory.getLogger(GroupOwnershipServiceImpl.class);

    private final GroupOwnershipRepository groupOwnershipRepository;
    private final PageableHelper pageableHelper;
    private final LoggedUser loggedUser;

    @Override
    public Response<GroupOwnershipDto> save(GroupOwnershipDto dto){
        try {

            Optional<GroupOwnership> optionalGroupOwnership = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && optionalGroupOwnership.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Group ownership could not be found or may have been deleted from the system",
                        null
                );
            }

            GroupOwnership groupOwnership = optionalGroupOwnership.orElse(new GroupOwnership());
            groupOwnership.setOwnershipType(dto.getOwnershipType());
            groupOwnership.setName(dto.getName());
            groupOwnership.setCreatedBy(loggedUser.getCurrentUserId());
            GroupOwnership saved = groupOwnershipRepository.save(groupOwnership);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    dto.getName()+" saved successfully",
                    convertToDto(saved)
            );
        }
        catch (Exception e){
            log.error("***** Error on saving group ownership type: {}",e.getMessage());
            return new Response<>(
                    false,
                    ResponseCode.INVALID_INPUT_DATA,
                    "Error when saving group ownership type",
                    null
            );
        }
    }
    @Override
    public Optional<GroupOwnership> getOptionalByUid(UUID uid){
        return uid != null ? groupOwnershipRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<GroupOwnershipDto> findByUid(UUID uid){
        try {
            Optional<GroupOwnership> optionalGroupOwnership = getOptionalByUid(uid);
            return optionalGroupOwnership.map(groupOwnership -> new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Success",
                    convertToDto(groupOwnership)
            )).orElseGet(() -> new Response<>(
                    false,
                    ResponseCode.NO_RECORD_FOUND,
                    "Group could not be found or may have been deleted from the system",
                    null
            ));
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Error when fetching group ownership data kindly contact support",
                    null
            );
        }
    }

    @Override
    public Response<GroupOwnershipDto> delete(UUID uid){
        try {
            Optional<GroupOwnership> optionalGroupOwnership = getOptionalByUid(uid);
            if (optionalGroupOwnership.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.DUPLICATE_RECORD,
                        "Group Ownership could not be found or may have been deleted from the system",
                        null
                );
            }
            GroupOwnership groupOwnership = optionalGroupOwnership.orElse(new GroupOwnership());
            groupOwnership.setActive(false);
            groupOwnership.setDeleted(true);
            groupOwnership.setDeletedAt(LocalDateTime.now());
            groupOwnership.setDeletedBy(loggedUser.getCurrentUserId());
            GroupOwnership saved = groupOwnershipRepository.save(groupOwnership);
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Group deleted successfully",
                    null
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    true,
                    ResponseCode.FAILURE,
                    "Error when deleting Group Ownership data",
                    null
            );
        }
    }

    @Override
    public Page<GroupOwnershipDto> searchGroupOwnerships(PageableParam pageableParam) {

        try {
            Pageable pageable = pageableHelper.buildPageable(pageableParam);

            GenericSpecificationSearch<GroupOwnership> genericSpec = new GenericSpecificationSearch<>();

            Specification<GroupOwnership> spec = Specification
                    .where(genericSpec.getSearchSpec(pageableParam.getSearchFields()))
                    .and(getHierarchyLevelSpecs());

            Page<GroupOwnership> groupOwnershipPage = groupOwnershipRepository.findAll(spec, pageable);
            return groupOwnershipPage.map(this::convertToDto);

        } catch (Exception e) {
            logger.error("Error in pageable group ownership search entities: {}", e.getMessage());
            return Page.empty();
        }
    }

    private Specification<GroupOwnership> getHierarchyLevelSpecs(){
        return (root,
                query,
                criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if(loggedUser.isSuperAdmin()){
                //can see all system users
                predicateList.add(criteriaBuilder.equal(root.get("active"), true));
            }
            //TO DO: need a logic to get all users that are for this Admin and his/ her level

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    @Override
    public List<GroupOwnership> groupOwnerships(){
        return groupOwnershipRepository.findAll();
    }

    private GroupOwnershipDto convertToDto(GroupOwnership groupOwnership) {
        GroupOwnershipDto dto = new GroupOwnershipDto();
        BeanUtils.copyProperties(groupOwnership, dto);
        return dto;
    }
}
