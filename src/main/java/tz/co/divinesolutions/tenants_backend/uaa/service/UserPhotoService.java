package tz.co.divinesolutions.tenants_backend.uaa.service;

import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.uaa.dto.UserPhotoData;
import tz.co.divinesolutions.tenants_backend.uaa.dto.UserPhotoDto;

import java.util.UUID;

public interface UserPhotoService {
    Response<UserPhotoData> updateProfileImage(UserPhotoDto dto);

    Response<UserPhotoData> getPhotoByUid(UUID uid);
}
