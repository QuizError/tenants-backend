package tz.co.divinesolutions.tenants_backend.utils;

import tz.co.divinesolutions.tenants_backend.entities.UserAccount;

import java.util.Optional;

public interface LoggedUser {
    UserAccount getCurrentUser();
    Optional<UserAccount> getCurrentUserOptional();
    String getCurrentUsername();

    boolean isSuperAdmin();

    Long getCurrentUserId();
    boolean isAuthenticated();
    boolean hasAuthority(String authority);
}