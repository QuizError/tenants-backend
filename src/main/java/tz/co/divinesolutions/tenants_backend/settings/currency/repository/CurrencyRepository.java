package tz.co.divinesolutions.tenants_backend.settings.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.Currency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    List<Currency>findAllByActiveTrue();
    Optional<Currency> findFirstByUid(UUID uid);
}
