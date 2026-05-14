package tz.co.divinesolutions.tenants_backend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    
    @Value("${app.jwt.access-token.expiration}")
    private int accessTokenExpirationMs;
    
    @Value("${app.jwt.refresh-token.expiration}")
    private int refreshTokenExpirationMs;
    
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    // CREATE ACCESS TOKEN
    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);
        claims.put("type", "access");

        assert userPrincipal != null;
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + accessTokenExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    // CREATE REFRESH TOKEN (with unique tokenId)
    public String generateRefreshToken(String username) {
        String tokenId = UUID.randomUUID().toString();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("tokenId", tokenId);  // store tokenId on token (access)
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + refreshTokenExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    // Get username from token
    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
    
    // Get tokenId from token
    public String getTokenIdFromToken(String token) {
        return (String) Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("tokenId");
    }
    
    // Get token type (access/refresh)
    public String getTokenType(String token) {
        return (String) Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("type");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // check if token is access token
    public boolean isAccessToken(String token) {
        try {
            return "access".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }
    
    // check if token is refresh token
    public boolean isRefreshToken(String token) {
        try {
            return "refresh".equals(getTokenType(token));
        } catch (Exception e) {
            return false;
        }
    }
    
    // Get time left
    public long getRemainingTime(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
            
            return expiration.getTime() - new Date().getTime();
        } catch (Exception e) {
            return 0;
        }
    }
}