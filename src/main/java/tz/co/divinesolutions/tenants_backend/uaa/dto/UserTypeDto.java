package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tz.co.divinesolutions.tenants_backend.enums.UserType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserTypeDto {
    private UserType userType;
}
