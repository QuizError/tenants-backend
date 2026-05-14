package tz.co.divinesolutions.tenants_backend.ownership.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnershipMember;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupMembershipData;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipMemberDto;
import tz.co.divinesolutions.tenants_backend.ownership.service.GroupOwnershipMemberService;
import tz.co.divinesolutions.tenants_backend.globals.Response;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("group-ownership-members")
public class GroupOwnershipMemberController {
    @Autowired
    private GroupOwnershipMemberService groupOwnershipMemberService;

    @PostMapping
    public Response<GroupMembershipData> save(@RequestBody GroupOwnershipMemberDto dto){
        return groupOwnershipMemberService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<GroupOwnershipMember> save(@PathVariable UUID uid){
        return groupOwnershipMemberService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<GroupOwnershipMember> delete(@PathVariable UUID uid){
        return groupOwnershipMemberService.delete(uid);
    }

    @GetMapping
    public Response<GroupOwnershipMember> groupOwnershipMemberList(){
        return groupOwnershipMemberService.groupOwnershipMembers();
    }

    @GetMapping("/members/{uid}")
    public Response<GroupOwnershipMember> getGroupMembers(@PathVariable UUID uid){
        return groupOwnershipMemberService.listGroupMembers(uid);
    }

    @GetMapping("/groups/{uid}")
    public Response<GroupOwnership> getMyGroups(@PathVariable UUID uid){
        return groupOwnershipMemberService.listMyGroups(uid);
    }
}
