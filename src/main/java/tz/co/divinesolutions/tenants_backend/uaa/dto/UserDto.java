package tz.co.divinesolutions.tenants_backend.uaa.dto;

import tz.co.divinesolutions.tenants_backend.enums.Gender;
import tz.co.divinesolutions.tenants_backend.enums.IDType;
import tz.co.divinesolutions.tenants_backend.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private UUID uid;
    private String firstname;
    private String lastname;
    private String middleName;
    private String fullName;
    private String email;
    private String mobile;
    private String password;
    private UserType userType;
    private IDType idType;
    private String dob;
    private String idNumber;
    private Gender gender;
}