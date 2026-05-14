package tz.co.divinesolutions.tenants_backend.ownership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tz.co.divinesolutions.tenants_backend.entities.GroupOwnership;
import tz.co.divinesolutions.tenants_backend.globals.*;
import tz.co.divinesolutions.tenants_backend.ownership.dto.GroupOwnershipDto;
import tz.co.divinesolutions.tenants_backend.ownership.service.GroupOwnershipService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("group-ownerships")
@RequiredArgsConstructor
public class GroupOwnershipController {

    private final GroupOwnershipService groupOwnershipService;
    private final PageableHelper pageableHelper;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CREATE_OWNERSHIP_GROUP','ROLE_EDIT_OWNERSHIP_GROUP')")
    public Response<GroupOwnershipDto> save(@RequestBody GroupOwnershipDto dto){
        return groupOwnershipService.save(dto);
    }

    @GetMapping("{uid}")
    @PreAuthorize("hasAuthority('ROLE_VIEW_OWNERSHIP_GROUP')")
    public Response<GroupOwnershipDto> findByUid(@PathVariable UUID uid){
        return groupOwnershipService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    @PreAuthorize("hasAuthority('ROLE_DELETE_OWNERSHIP_GROUP')")
    public Response<GroupOwnershipDto> delete(@PathVariable UUID uid){
        return groupOwnershipService.delete(uid);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW_OWNERSHIP_GROUPS')")
    public List<GroupOwnership> groupOwnerships(){
        return groupOwnershipService.groupOwnerships();
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_VIEW_OWNERSHIP_GROUPS')")
    public ResponseEntity<Response<PageResponse<GroupOwnershipDto>>> searchHierarchyLevels(
            @RequestBody PageableParam pageableParam) {
        Page<GroupOwnershipDto> usersPage = groupOwnershipService.searchGroupOwnerships(pageableParam);

        PageResponse<GroupOwnershipDto> responseData = pageableHelper.toPageResponse(usersPage);

        return ResponseEntity.ok(
                new Response<>(
                        true,
                        ResponseCode.SUCCESS,
                        "Groups listed successfully",
                        responseData)
        );
    }
}
