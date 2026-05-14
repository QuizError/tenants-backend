package tz.co.divinesolutions.tenants_backend.uaa.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserPhotoData {
    private UUID uid;
    private String base64Content;
    private String imagePath;
    private String fullName;
}
