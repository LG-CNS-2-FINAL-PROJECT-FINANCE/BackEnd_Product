package com.ddiring.BackEnd_Product.s3.upload;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Document("upload_bin")
public class UploadEntry {
    @Id
    private String id;

    @Indexed
    private String userSeq;          // 업로더 식별 (JWT에서 추출)

    @Indexed
    private Type type;               // IMAGE or DOCUMENT

    private String s3Key;
    private String url;
    private String contentType;
    private long size;

    @Indexed
    private Status status;           // TEMP → ATTACHED

    private String productId;        // 붙인 뒤 채움(없으면 null)
    private Instant createdAt;

    public enum Type { IMAGE, DOCUMENT }
    public enum Status { TEMP, ATTACHED }
}
