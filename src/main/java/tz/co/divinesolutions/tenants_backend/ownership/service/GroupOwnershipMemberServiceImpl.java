package tz.co.divinesolutions.tenants_backend.ownership.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.entities.*;
import tz.co.divinesolutions.tenants_backend.enums.OwnershipMemberStatus;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupMembershipData;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipMemberDto;
import tz.co.divinesolutions.tenants_backend.ownership.repository.GroupOwnershipMemberRepository;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserAccountRepository;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupOwnershipMemberServiceImpl implements GroupOwnershipMemberService{

    private final GroupOwnershipMemberRepository groupOwnershipMemberRepository;
    private final GroupOwnershipService groupOwnershipService;
    private final UserAccountRepository userAccountRepository;
    private final LoggedUser loggedUser;

    @Override
    public Response<GroupMembershipData> save(GroupOwnershipMemberDto dto){
        try {
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUid(dto.getUserUid());
            if (optionalUserAccount.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "User could not be found or may have been deleted from the system",
                        null
                );
            }
            UserAccount user = optionalUserAccount.get();

            if (!loggedUser.isSuperAdmin() || loggedUser.getCurrentUser() != user){
                return new Response<>(
                        false,
                        ResponseCode.UNAUTHORIZED,
                        "You are not authorized to create membership for another user",
                        null
                        );
            }

            Optional<GroupOwnership> optionalGroup = groupOwnershipService.getOptionalByUid(dto.getGroupUid());
            if (optionalGroup.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Group could not be found or may have been deleted from the system",
                        null);
            }
            GroupOwnership group = optionalGroup.get();

            Optional<GroupOwnershipMember> optionalGroupOwnershipMember = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && optionalGroupOwnershipMember.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Group (owner) member unit could not be found or may have been deleted from the system",
                        null
                );
            }
            OwnershipMemberStatus memberStatus = dto.getMemberStatus() == null ?  OwnershipMemberStatus.ACTIVE : dto.getMemberStatus();

            GroupOwnershipMember groupOwnershipMember = optionalGroupOwnershipMember.orElse(new GroupOwnershipMember());
            groupOwnershipMember.setUser(user);
            groupOwnershipMember.setGroup(group);
            groupOwnershipMember.setMemberStatus(memberStatus);
            GroupOwnershipMember saved = groupOwnershipMemberRepository.save(groupOwnershipMember);

            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Group (owners) member saved successfully",
                    convertToDto(saved)
            );
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Error when saving Group (owners) member",
                    null
            );
        }
    }

    public Optional<GroupOwnershipMember> getOptionalByUid(UUID uid){
        return uid != null ? groupOwnershipMemberRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<GroupOwnershipMember> findByUid(UUID uid){
        try {
            Optional<GroupOwnershipMember> optionalProperty = getOptionalByUid(uid);
            return optionalProperty.map(property -> new Response<>(true, ResponseCode.SUCCESS, "Success", property)).orElseGet(() -> new Response<>(false, ResponseCode.DUPLICATE_RECORD, "Group (owners) Member could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Group Ownership: {}",e.getMessage());
            return new Response<>(
                    true,
                    ResponseCode.FAILURE,
                    "Error when fetching group (owners) member data",
                    null
            );
        }
    }

    @Override
    public Response<GroupOwnershipMember> delete(UUID uid){
        try {
            Optional<GroupOwnershipMember> optionalGroupOwnershipMember = getOptionalByUid(uid);
            if (optionalGroupOwnershipMember.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE_RECORD, "Group (owners) Member could not be found or may have been deleted from the system", null);
            }
            groupOwnershipMemberRepository.delete(optionalGroupOwnershipMember.get());
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    "Group (owners) Member deleted successfully",
                    null
            );
        }
        catch (Exception e){
            log.error("***** Error on fetching Group (owners) Member: {}",e.getMessage());
            return new Response<>(
                    true,
                    ResponseCode.FAILURE,
                    "Error when deleting Group (owners) Member data",
                    null
            );
        }
    }

    @Override
    public Response<GroupOwnershipMember> groupOwnershipMembers(){
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                groupOwnershipMemberRepository.findAll(),
                "Success"
        );
    }

    @Override
    public Response<GroupOwnershipMember> listGroupMembers(UUID groupUid){
        try {
            Optional<GroupOwnership> optionalGroup = groupOwnershipService.getOptionalByUid(groupUid);
            if (optionalGroup.isEmpty()){
                return new Response<>(
                        true,
                        ResponseCode.NO_RECORD_FOUND,
                        Collections.emptyList(),
                        "Success"
                );
            }
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    groupOwnershipMemberRepository.findAllByGroup(optionalGroup.get()),
                    "Success"
            );
        }
        catch (Exception e){
            log.error("Error when fetching group members");
            return new Response<>(
                    true,
                    ResponseCode.FAILURE,
                    Collections.emptyList(),
                    "Success"
            );
        }
    }

    @Override
    public Response<GroupOwnership> listMyGroups(UUID userUid){
        try {
            Optional<UserAccount> optionalUser = userAccountRepository.findFirstByUid(userUid);
            if (optionalUser.isPresent()){
                 UserAccount user = optionalUser.get();
                List<GroupOwnership> myGroups = new ArrayList<>();
                List<GroupOwnershipMember> myGroupMemberships = groupOwnershipMemberRepository.findAllByUser(user);
                for (GroupOwnershipMember membership : myGroupMemberships){
                    myGroups.add(membership.getGroup());
                }
                return new Response<>(
                        true,
                        ResponseCode.SUCCESS,
                        myGroups,
                        "Success"
                );
            }
            else {
                return new Response<>(
                        true,
                        ResponseCode.NO_RECORD_FOUND,
                        Collections.emptyList(),
                        "Success"
                );
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    true,
                    ResponseCode.SUCCESS,
                    Collections.emptyList(),
                    "Success"
            );
        }
    }

    private GroupMembershipData convertToDto(GroupOwnershipMember groupOwnershipMember) {
        GroupMembershipData dto = new GroupMembershipData();
        BeanUtils.copyProperties(groupOwnershipMember, dto);
        dto.setMemberName(groupOwnershipMember.getUser().getFullName());
        dto.setGroupName(groupOwnershipMember.getGroup().getName());
        return dto;
    }

}
