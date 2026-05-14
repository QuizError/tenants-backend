package tz.co.divinesolutions.tenants_backend.uaa.dto;

import tz.co.divinesolutions.tenants_backend.enums.Gender;
import tz.co.divinesolutions.tenants_backend.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserAccountData {
    private UUID uid;
    private Long id;
    private String msisdn;
    private String firstname;
    private String lastname;
    private String fullName;
    private Gender gender;
    private String email;
    private String imagePath;
    private UserType userType;
    private LocalDateTime lastLogin;
}