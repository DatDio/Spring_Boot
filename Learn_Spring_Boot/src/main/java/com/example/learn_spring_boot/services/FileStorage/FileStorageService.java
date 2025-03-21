package com.example.learn_spring_boot.services.FileStorage;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public String saveImage(MultipartFile file) {
        try {
            File uploadFolder = new File(UPLOAD_DIR);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // Tạo tên file mới với UUID
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Lưu ảnh
            Path path = Paths.get(UPLOAD_DIR + newFileName);
            Files.write(path, file.getBytes());

            return  newFileName;
        } catch (IOException e) {
            return "";
        }
    }
    public boolean deleteImage(String fileName) {
        try {
            if (StringUtils.isEmpty(fileName)) {
                return false; // Nếu tên file rỗng thì không xóa
            }

            Path path = Paths.get(UPLOAD_DIR + fileName);
            File file = path.toFile();

            if (file.exists()) {
                return file.delete(); // Xóa file
            }

            return false; // Ảnh không tồn tại
        } catch (Exception e) {
            return false;
        }
    }

}

