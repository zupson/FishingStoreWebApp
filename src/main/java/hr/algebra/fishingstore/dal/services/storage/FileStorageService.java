package hr.algebra.fishingstore.dal.services.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String saveImage(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" +
                    StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uploadDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not save image: " + e.getMessage());
        }
    }

    public void deleteImage(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete image: " + e.getMessage());
        }
    }
}

