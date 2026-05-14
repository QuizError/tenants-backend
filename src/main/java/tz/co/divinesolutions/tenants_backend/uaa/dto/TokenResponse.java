package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private LocalDateTime expiresAt;
    private Long id;
    private String username;
    private String email;
    private List<String> authorities;
}