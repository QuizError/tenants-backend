package tz.co.divinesolutions.tenants_backend.utils;

import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import tz.co.divinesolutions.tenants_backend.exceptions.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class LoggedUserImpl implements LoggedUser {
    
    @Override
    public UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not logged in");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserAccount) {
            return (UserAccount) principal;
        }
        
        throw new UnauthorizedException("User type could not be defined");
    }
    
    @Override
    public Optional<UserAccount> getCurrentUserOptional() {
        try {
            return Optional.of(getCurrentUser());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    @Override
    public boolean isSuperAdmin(){
        return getCurrentUser().isSuperAdmin();
    }
    
    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
    
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
    
    @Override
    public boolean hasAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                       .map(GrantedAuthority::getAuthority).filter(Objects::nonNull)
                   .anyMatch(auth -> auth.equals(authority));
    }
}