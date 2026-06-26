package com.samteo.domain.community.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Service
public class CommunityImageStorageService {

    private static final String STORAGE_TYPE_S3 = "s3";

    private final String storageType;
    private final Path uploadRoot;
    private final String uploadUrlPrefix;
    private final String bucket;
    private final String publicBaseUrl;
    private final S3Client s3Client;

    public CommunityImageStorageService(
            @Value("${app.storage-type:local}") String storageType,
            @Value("${app.upload-dir:uploads}") String uploadDir,
            @Value("${app.upload-url-prefix:/uploads}") String uploadUrlPrefix,
            @Value("${aws.region:ap-northeast-2}") String region,
            @Value("${aws.s3.bucket:}") String bucket,
            @Value("${aws.s3.public-base-url:}") String publicBaseUrl
    ) {
        this.storageType = storageType == null ? "local" : storageType.toLowerCase(Locale.ROOT);
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.uploadUrlPrefix = uploadUrlPrefix.endsWith("/")
                ? uploadUrlPrefix.substring(0, uploadUrlPrefix.length() - 1)
                : uploadUrlPrefix;
        this.bucket = bucket;
        this.publicBaseUrl = normalizePublicBaseUrl(publicBaseUrl);
        this.s3Client = STORAGE_TYPE_S3.equals(this.storageType)
                ? S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()
                : null;
    }

    public String storeCommunityImage(MultipartFile file, Long postId) {
        validate(file, postId);

        String contentType = file.getContentType();
        String extension = extensionOf(file.getOriginalFilename(), contentType);
        String fileName = UUID.randomUUID() + extension;

        if (STORAGE_TYPE_S3.equals(storageType)) {
            return storeToS3(file, postId, fileName, contentType);
        }
        return storeToLocal(file, postId, fileName);
    }

    private void validate(MultipartFile file, Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 필요합니다.");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 이미지 파일은 업로드할 수 없습니다.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String storeToS3(MultipartFile file, Long postId, String fileName, String contentType) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalStateException("AWS_S3_BUCKET 설정이 필요합니다.");
        }

        String key = "community/" + postId + "/" + fileName;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));
            return publicUrl(key);
        } catch (S3Exception e) {
            throw new IllegalStateException(
                    "S3 image upload failed. bucket=" + bucket
                            + ", key=" + key
                            + ", awsStatus=" + e.statusCode()
                            + ", awsCode=" + e.awsErrorDetails().errorCode()
                            + ", awsMessage=" + e.awsErrorDetails().errorMessage(),
                    e
            );
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일 업로드에 실패했습니다.", e);
        }
    }

    private String storeToLocal(MultipartFile file, Long postId, String fileName) {
        try {
            Path directory = uploadRoot.resolve("community").resolve(String.valueOf(postId)).normalize();
            Files.createDirectories(directory);

            Path target = directory.resolve(fileName).normalize();
            if (!target.startsWith(directory)) {
                throw new IllegalArgumentException("잘못된 파일 경로입니다.");
            }

            file.transferTo(target);
            return uploadUrlPrefix + "/community/" + postId + "/" + fileName;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일 저장에 실패했습니다.", e);
        }
    }

    private String publicUrl(String key) {
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return publicBaseUrl + "/" + key;
        }
        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder().bucket(bucket).key(key).build())
                .toString();
    }

    private String normalizePublicBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
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
