package tz.co.divinesolutions.tenants_backend.uaa.repository;

import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long>, JpaSpecificationExecutor<UserAccount> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findFirstByUid(UUID uid);
    boolean existsByMsisdn(String mobile);
    boolean existsByEmailAndEmailIsNotNull(String email);
    boolean existsByMsisdnAndFirstnameIgnoreCase(String msisdn, String firstname);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount u SET u.firstLoginAt = :now WHERE u.id = :userId AND u.firstLoginAt IS NULL")
    void updateFirstLoginIfNull(Long userId, LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount SET lastLogin = :now WHERE id = :userId")
    void updateLastLogin(Long userId, LocalDateTime now);
}
