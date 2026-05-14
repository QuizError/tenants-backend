package tz.co.divinesolutions.tenants_backend.entities;

import tz.co.divinesolutions.tenants_backend.enums.Gender;
import tz.co.divinesolutions.tenants_backend.enums.IDType;
import tz.co.divinesolutions.tenants_backend.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserAccount extends BaseEntity implements UserDetails {

    private String firstname;
    private String middleName;
    private String lastname;
    private String email;
    private String msisdn;
    private String username;
    private String fullName;
    private String imagePath;
    private LocalDate dob;

    @Column(length = 30)
    private String idNumber;

    @Enumerated(EnumType.STRING)
    private IDType idType;

    private LocalDateTime lastLogin;
    private LocalDateTime firstLoginAt;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean enabled = true;

    private boolean verified =  false;

    private boolean isSuperAdmin = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // For each user role
        for (Role role : roles) {
            // add role (eg: "ROLE_LOAN_OFFICER")
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            // add all permissions for each role (eg: "CREATE_LOAN")
            for (Permission permission : role.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
