package tz.co.divinesolutions.tenants_backend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Slf4j
@Service
public class GetImageData {
    public Response<String> getImageAsBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            String mimeType = Files.probeContentType(path);
            String base64String = "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
            return new Response<>(true, ResponseCode.SUCCESS, "Photo retrieved successfully", base64String);

        } catch (NoSuchFileException e) {
            log.error("Photo not found: {}", e.getMessage());
            return new Response<>(false, ResponseCode.NO_RECORD_FOUND, "Photo not found", null);
        } catch (Exception e) {
            log.error("Failed to retrieve the Photo: {}", e.getMessage());
            return new Response<>(false, ResponseCode.FAILURE, "Failed to retrieve the Photo", null);
        }
    }
}