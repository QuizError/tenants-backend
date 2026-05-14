package tz.co.divinesolutions.tenants_backend.uaa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tz.co.divinesolutions.tenants_backend.entities.UserAccount;
import tz.co.divinesolutions.tenants_backend.entities.UserPhoto;
import tz.co.divinesolutions.tenants_backend.globals.Response;
import tz.co.divinesolutions.tenants_backend.globals.ResponseCode;
import tz.co.divinesolutions.tenants_backend.uaa.dto.UserPhotoData;
import tz.co.divinesolutions.tenants_backend.uaa.dto.UserPhotoDto;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserAccountRepository;
import tz.co.divinesolutions.tenants_backend.uaa.repository.UserPhotoRepository;
import tz.co.divinesolutions.tenants_backend.utils.GetImageData;
import tz.co.divinesolutions.tenants_backend.utils.LoggedUser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPhotoServiceImpl implements UserPhotoService{
    private final LoggedUser loggedUser;
    private final GetImageData getImageData;
    private final UserPhotoRepository userPhotoRepository;
    private final UserAccountRepository userAccountRepository;

    @Value("${file.upload-dir}")
    private String uploadBaseDir;

    @Override
    @Transactional
    public Response<UserPhotoData> updateProfileImage(UserPhotoDto dto) {
        UserAccount currentUser = loggedUser.getCurrentUser();
        if (currentUser == null){
            return new Response<>(false, ResponseCode.UNAUTHORIZED,
                    "Not authorized to perform the action, contact Administrator", null);
        }

        Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUid(dto.getUserUid());
        if (optionalUserAccount.isEmpty()){
            return new Response<>(
                    false,
                    ResponseCode.NO_RECORD_FOUND,
                    "User could not be found or may have been deleted from the system",
                    null);
        }
        UserAccount userAccount = optionalUserAccount.get();

        if (userPhotoRepository.existsByUserAccountAndActiveTrue(userAccount)){
            userPhotoRepository.deleteAllByUserAccount(userAccount);
            userPhotoRepository.flush();
        }
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserAccount(userAccount);

        if (dto.getBase64Image() != null && !dto.getBase64Image().isEmpty()) {
            String imagePath = getImagePath(dto.getBase64Image());
            if (!imagePath.isEmpty()) {
                userPhoto.setImagePath(imagePath);
                userPhoto.setImagePath(imagePath);
                userPhotoRepository.save(userPhoto);

                //update on user account
                userAccount.setImagePath(imagePath);
                userAccountRepository.save(userAccount);
            } else {
                return new Response<>(
                        false,
                        ResponseCode.INVALID_INPUT_DATA,
                        "Could not update user image",
                        null);
            }
        }
        userPhotoRepository.save(userPhoto);
        return new Response<>(
                true,
                ResponseCode.SUCCESS,
                "Profile photo saved successfully",
                convertToDto(userPhoto,""));
    }


    private String getImagePath(String base64Data) {
        try {
            String mimeType = null;
            if (base64Data.contains(",")) {
                String[] parts = base64Data.split(",");
                base64Data = parts[1];
                mimeType = parts[0].split(":")[1].split(";")[0];
            }

            byte[] fileBytes = Base64.getDecoder().decode(base64Data);

            assert mimeType != null;
            String fileExtension = getFileExtension(mimeType);
            String filename = "tmis_user" + UUID.randomUUID().toString() + "." + fileExtension;
            Path filePath = Paths.get(uploadBaseDir+ "images/",filename);
//            Path filePath = Paths.get("/data/uploads/tmis/", filename);
            Files.createDirectories(filePath.getParent());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            Files.write(filePath, fileBytes);
            return filePath.toString();

        } catch (Exception e) {
            System.err.println("Failed to save/update the file: " + e.getMessage());
            return "";
        }
    }

    private String getFileExtension(String mimeType) {
        return switch (mimeType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "unknown";
        };
    }

    @Override
    public Response<UserPhotoData> getPhotoByUid(UUID uid) {
        try {
            log.info("*** {} looking for user photo.....", loggedUser.getCurrentUser().getFullName());
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUid(uid);
            if (optionalUserAccount.isEmpty()){
                return new Response<>(
                        false,
                        ResponseCode.NO_RECORD_FOUND,
                        "Could not find user profile photo",
                        null);
            }
            UserAccount userAccount = optionalUserAccount.get();

            Optional<UserPhoto> optionalUserPhoto = userPhotoRepository.findByUserAccount(userAccount);
            UserPhoto photo = optionalUserPhoto.orElse(null);
            assert photo != null;
            log.info("Path for {} Photo is: {}", userAccount.getFullName(), photo.getImagePath());

            Response<String> base64 = getImageData.getImageAsBase64(photo.getImagePath());
            log.info("Base64 Generated is: {}", base64.getData());
            if(base64.getStatus()){
                return new Response<>(
                        true,
                        ResponseCode.SUCCESS,
                        "Success",
                        convertToDto(photo,base64.getData()));
            }
            else {
                return new Response<>(
                        false,
                        ResponseCode.FAILURE,
                        "Could not generate base64",
                        null);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new Response<>(
                    false,
                    ResponseCode.FAILURE,
                    "Error when fetching user profile photo",
                    null);
        }
    }

    private UserPhotoData convertToDto(UserPhoto userPhoto, String base64) {
        UserPhotoData dto = new UserPhotoData();
        BeanUtils.copyProperties(userPhoto, dto);
        dto.setBase64Content(base64);
        dto.setFullName(userPhoto.getUserAccount().getFullName());
        return dto;
    }
}
