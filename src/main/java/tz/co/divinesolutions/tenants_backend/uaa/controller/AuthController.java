package tz.co.divinesolutions.tenants_backend.uaa.controller;

import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordRequest;
import tz.co.divinesolutions.tenants_backend.sms.dto.ForgotPasswordResponse;
import tz.co.divinesolutions.tenants_backend.uaa.dto.*;
import tz.co.divinesolutions.tenants_backend.uaa.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAccountService userAccountService;

    @PostMapping("/registration")
    public Response<UserAccountData> createUser(@Valid @RequestBody UserDto userDto) {
        return userAccountService.createNewUser(userDto);
    }

    @PostMapping("/login")
    public Response<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userAccountService.login(loginRequest);
    }

    @PostMapping("/forgot-password")
    public Response<ForgotPasswordResponse> forgotPasswordRequest(
            @Valid @RequestBody ForgotPasswordRequest dto) {
        return userAccountService.forgotPasswordRequest(dto);
    }

    @PostMapping("/refresh-token")
    public Response<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return userAccountService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequest request,
                                    HttpServletRequest httpRequest) {
        return userAccountService.logout(request, httpRequest);
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutFromAllDevices(@Valid @RequestBody LogoutRequest request) {
        return userAccountService.logoutFromAllDevices(request);
    }
}