package tz.co.divinesolutions.tenants_backend.uaa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import tz.co.divinesolutions.tenants_backend.entities.UserPhoto;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto,Long> {
    Optional<UserPhoto> findFirstByUid(UUID uid);
    Optional<UserPhoto> findByUserAccount(UserAccount userAccount);
    boolean existsByUserAccountAndActiveTrue(UserAccount userAccount);
    void deleteAllByUserAccount(UserAccount userAccount);
}
