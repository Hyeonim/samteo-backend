package com.samteo.domain.community.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Service
public class CommunityImageStorageService {

    private final Path uploadRoot;
    private final String uploadUrlPrefix;

    public CommunityImageStorageService(
            @Value("${app.upload-dir:uploads}") String uploadDir,
            @Value("${app.upload-url-prefix:/uploads}") String uploadUrlPrefix
    ) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.uploadUrlPrefix = uploadUrlPrefix.endsWith("/")
                ? uploadUrlPrefix.substring(0, uploadUrlPrefix.length() - 1)
                : uploadUrlPrefix;
    }

    public String storeCommunityImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 이미지 파일은 업로드할 수 없습니다.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        try {
            Path directory = uploadRoot.resolve("community").normalize();
            Files.createDirectories(directory);
            String extension = extensionOf(file.getOriginalFilename(), contentType);
            String fileName = UUID.randomUUID() + extension;
            Path target = directory.resolve(fileName).normalize();
            if (!target.startsWith(directory)) {
                throw new IllegalArgumentException("잘못된 파일 경로입니다.");
            }
            file.transferTo(target);
            return uploadUrlPrefix + "/community/" + fileName;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일 저장에 실패했습니다.", e);
        }
    }

    private String extensionOf(String originalFilename, String contentType) {
        if (originalFilename != null) {
            int dot = originalFilename.lastIndexOf('.');
            if (dot >= 0 && dot < originalFilename.length() - 1) {
                String ext = originalFilename.substring(dot).toLowerCase(Locale.ROOT);
                if (ext.matches("\\.(jpg|jpeg|png|gif|webp)")) {
                    return ext;
                }
            }
        }
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".jpg";
        };
    }
}
