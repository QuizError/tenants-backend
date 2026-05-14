package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AssignRolesResponse {
    private String fullName;
    private List<String> authorities;
}
