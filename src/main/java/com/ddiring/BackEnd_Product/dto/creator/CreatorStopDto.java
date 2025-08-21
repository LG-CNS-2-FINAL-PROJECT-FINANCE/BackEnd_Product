package com.ddiring.BackEnd_Product.dto.creator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatorStopDto {

    @NotBlank(message="대상 프로젝트 ID 필요")
    private String projectId;

    @NotNull(message = "파일을 등록하세요")
    private List<String> document;
    private List<String> image;

    @Size(max = 500)
    @NotBlank(message = "사유를 입력하세요")
    private String reason;
}
