package com.example.learn_spring_boot.controllers.admin;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.services.FileStorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileUploadController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = fileStorageService.uploadFile(file);
        if (!StringUtils.isEmpty(imageUrl)) {
            return ApiResponse.success("Upload file succesfully", imageUrl);
        }
        return ApiResponse.success("Upload file failure", "");
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> deleteImage(@RequestParam("imageUrl") String imageUrl) {
        fileStorageService.deleteImage(imageUrl);
        return ResponseEntity.ok(Map.of("message", "Xóa ảnh thành công"));
    }
}
