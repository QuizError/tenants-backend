package tz.co.divinesolutions.tenants_backend.uaa.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogoutRequest {
    @NotBlank(message = "Refresh token must be filled")
    private String refreshToken;
}