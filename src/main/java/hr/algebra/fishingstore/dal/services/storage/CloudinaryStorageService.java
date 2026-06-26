package hr.algebra.fishingstore.dal.services.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryStorageService {
    public static final String PRODUCTS_IMAGE_DIR = "fishing-store/products";
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file) {
        try {
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", PRODUCTS_IMAGE_DIR)
            );
            return (String) result.get("public_id");
        } catch (IOException e) {
            log.error("Cloudinary upload failed for file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void delete(String publicId){
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            log.error("Cloudinary delete failed for publicId: {}", publicId, e);
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }
}