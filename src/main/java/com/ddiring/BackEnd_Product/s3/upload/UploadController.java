package com.ddiring.BackEnd_Product.s3.upload;

import com.ddiring.BackEnd_Product.s3.storage.S3StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class UploadController {

    private final S3StorageService s3;
    private final UploadEntryRepository repo;

    private String currentUser(HttpServletRequest req) {
        String v = req.getHeader("userSeq"); // 네 프로젝트에서 이미 사용하던 헤더
        if (v == null || v.isBlank()) throw new IllegalStateException("인증 정보 없음");
        return v.trim();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("type") UploadEntry.Type type, // IMAGE or DOCUMENT
            HttpServletRequest req
    ) throws Exception {
        String userSeq = currentUser(req);

        // 간단한 화이트리스트 (원하면 더 강화)
        String ct = file.getContentType();
        if (type == UploadEntry.Type.IMAGE) {
            if (ct == null || !ct.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "이미지 형식만 허용"));
            }
        }

        S3StorageService.UploadResult r = s3.upload(file.getBytes(), file.getOriginalFilename(), ct,
                type == UploadEntry.Type.IMAGE ? "temp/images" : "temp/docs");

        UploadEntry saved = repo.save(UploadEntry.builder()
                .userSeq(userSeq)
                .type(type)
                .s3Key(r.key())
                .url(r.url())
                .contentType(ct == null ? MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE : ct)
                .size(r.size())
                .status(UploadEntry.Status.TEMP)
                .createdAt(Instant.now())
                .build());

        return ResponseEntity.ok(Map.of(
                "assetId", saved.getId(),
                "type", saved.getType().name(),
                "url", saved.getUrl()
        ));
    }
}
