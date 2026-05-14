package tz.co.divinesolutions.tenants_backend.uaa.service;


import tz.co.divinesolutions.tenants_backend.entities.RefreshToken;
import tz.co.divinesolutions.tenants_backend.uaa.repository.RefreshTokenRepository;
import tz.co.divinesolutions.tenants_backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    
    @Value("${app.jwt.refresh-token.expiration}")
    private int refreshTokenExpirationMs;

    @Transactional
    public RefreshToken createRefreshToken(String username, String refreshToken) {
        String tokenId = jwtUtils.getTokenIdFromToken(refreshToken);
        Instant expiryDate = Instant.now().plusMillis(refreshTokenExpirationMs);
        
        // Kwanza, futa token zozote za zamani za mtumiaji huyu (optional)
        // Unaweza kuziacha au kuzifuta - napendekeza kuzifuta ili kuepuka mrundikano
        List<RefreshToken> existingTokens = refreshTokenRepository.findByUsername(username);
        if (existingTokens.size() >= 3) {  // Kila mtumiaji aweze na max token 3
            // Futa ya zamani zaidi
            RefreshToken oldest = existingTokens.getFirst();
            refreshTokenRepository.delete(oldest);
        }
        
        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .token(refreshToken)
            .username(username)
            .expiryDate(expiryDate)
            .tokenId(tokenId)
            .revoked(false)
            .build();
        
        return refreshTokenRepository.save(refreshTokenEntity);
    }
    
    public boolean validateRefreshToken(String username, String refreshToken) {
        // 1. Kagua kama token yenyewe ni sahihi (signature)
        if (!jwtUtils.validateToken(refreshToken) || !jwtUtils.isRefreshToken(refreshToken)) {
            return false;
        }
        
        // 2. Tafuta token kwenye database
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        RefreshToken storedToken = tokenOpt.get();
        
        // 3. Angalia kama imefutwa (revoked)
        if (storedToken.isRevoked()) {
            return false;
        }
        
        // 4. Angalia kama username inalingana
        if (!storedToken.getUsername().equals(username)) {
            return false;
        }
        
        // 5. Angalia kama muda haujaisha
        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            // Futa token iliyoisha
            refreshTokenRepository.delete(storedToken);
            return false;
        }
        
        return true;
    }

    @Transactional
    public void removeRefreshToken(String username, String refreshToken) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);
        
        tokenOpt.ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            log.info("Refresh token revoked for user: {} ", username);
        });
    }
    
    // Futa token zote za mtumiaji (logout kutoka sehemu zote)
    @Transactional
    public void removeAllUserTokens(String username) {
        List<RefreshToken> userTokens = refreshTokenRepository.findByUsername(username);
        
        for (RefreshToken token : userTokens) {
            token.setRevoked(true);
        }
        
        refreshTokenRepository.saveAll(userTokens);
        log.info("{} user tokens have been deleted", username);
    }
    
    // Weka access token kwenye blacklist (kwa kutumia map kwenye memory)
    // Kwa sababu access token zinaisha haraka, tumia memory cache
    private final java.util.Map<String, Boolean> blacklistStore = new java.util.concurrent.ConcurrentHashMap<>();
    
    public void blacklistAccessToken(String accessToken) {
        long remainingTime = jwtUtils.getRemainingTime(accessToken);
        if (remainingTime > 0) {
            blacklistStore.put(accessToken, true);
            log.info("Access token blacklisted");
        }
    }
    
    public boolean isTokenBlacklisted(String accessToken) {
        return blacklistStore.containsKey(accessToken);
    }
    
    // Futa token zilizoisha muda (itaweza kuitwa na scheduled job)
    @Transactional
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredSince(Instant.now());
        log.info("expired tokens have been deleted");
    }
    
    // Pata token kwa tokenId (kwa ajili ya kuangalia duplicates)
    public Optional<RefreshToken> findByTokenId(String tokenId) {
        return refreshTokenRepository.findByTokenId(tokenId);
    }
}