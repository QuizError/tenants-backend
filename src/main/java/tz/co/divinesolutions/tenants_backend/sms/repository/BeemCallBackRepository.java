package tz.co.divinesolutions.tenants_backend.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.SMSCallBackHistory;

@Repository
public interface BeemCallBackRepository extends JpaRepository<SMSCallBackHistory,Long> {
}