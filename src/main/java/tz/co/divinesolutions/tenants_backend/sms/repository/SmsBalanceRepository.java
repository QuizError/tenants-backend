package tz.co.divinesolutions.tenants_backend.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.SMSBalance;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SmsBalanceRepository extends JpaRepository<SMSBalance,Long> {
    Optional<SMSBalance> findFirstByCreatedAt(LocalDate localDate);
}