package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.common.response.dto.ApiResponseDto;
import com.ddiring.BackEnd_Product.dto.escrow.EscrowDistributionDto;
import com.ddiring.BackEnd_Product.dto.asset.AssetAccountDto;
import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.external.AssetClient;
import com.ddiring.BackEnd_Product.external.EscrowClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository pr;
    private final MongoTemplate mt;
    private final AssetClient ac;
    private final EscrowClient ec;
    private final ProductRequestRepository prr;

    // 모든 상품 조회
    public List<ProductListDto> getAllProject() {
        return pr.findAll().stream()
                .map(ProductListDto::from)
                .collect(Collectors.toList());
    }

    // 상품 상세 조회
    public ProductDetailDto getProductByProjectId(String projectId, String userSeq) {
        viewCount(projectId);
        ProductEntity product = pr.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
        return ProductDetailDto.from(product, userSeq);
    }

    public void viewCount(String projectId) {
        mt.getCollection("product") // 실제 컬렉션 이름
                .updateOne(
                        new Document("_id", projectId),
                        new Document("$inc", new Document("viewCount", 1))  
                );
    }

    // 모금액 저장 및 달성률 계산
    public void receiveAmount(AmountDto dto) {
        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        BigDecimal newAmount = dto.getBalance();
        if (newAmount == null) return;

        // 금액 변경 시에만 반영
        if (pe.getAmount() == null || pe.getAmount().compareTo(newAmount) != 0) {
            pe.setAmount(newAmount);

            // 달성률 계산
            BigDecimal goal = pe.getGoalAmount();
            BigDecimal percent = BigDecimal.ZERO;
            if (goal != null && goal.compareTo(BigDecimal.ZERO) > 0) {
                percent = newAmount
                        .divide(goal, 4, RoundingMode.HALF_UP)  // 소수 넉넉히 계산
                        .multiply(new BigDecimal("100"))
                        .min(new BigDecimal("100"));            // 100% 이상 방지
            }
            pe.setPercent(percent.setScale(1, RoundingMode.HALF_UP));

            // 상태 변경: 100% 달성 시 END
            if (percent.compareTo(new BigDecimal("100")) >= 0
                    && pe.getProjectStatus() == ProductEntity.ProjectStatus.OPEN) {
                pe.setProjectStatus(ProductEntity.ProjectStatus.FUNDING_LOCKED);
            }

            pr.save(pe);
        }
    }

    // Asset에 계좌 정보 전송
    @Transactional
    public ProductEntity sendAssetAccount(AssetAccountDto dto) {
        if (dto == null || dto.getProjectId() == null) {
            throw new IllegalArgumentException("projectId가 필요합니다.");
        }

        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // DB 내용으로 최종 DTO를 확정하고 싶으면 여기서 dto 보정 가능
        // (예: dto.setTitle(pe.getTitle());)
        ac.assetAccount(dto); // 동기 호출
        return pe;
    }

    // Escrow에 분배 정보 전송
    @Transactional
    public ProductEntity sendEscrowDistribution(String requestId) {
        ProductRequestEntity pre = prr.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분배 요청입니다."));

        ProductEntity pe = pr.findById(pre.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        BigDecimal distributionAmount = pre.getPayload().getDistributionAmount();
        if (distributionAmount == null) {
            throw new IllegalStateException("분배 금액이 설정되지 않았습니다.");
        }

        EscrowDistributionDto finalDto = EscrowDistributionDto.builder()
                .Amount(pe.getAmount())
                .userSeq(pre.getUserSeq())              // 요청자/투자자 userSeq
                .Amount(distributionAmount)
                .transType(3)                           // 무조건 3
                .build();

        // API 호출 결과 확인
        ApiResponseDto<String> response = ec.escrowDistribution(finalDto);
        log.info("Escrow 분배 응답: {}", response);   // 로그 확인용

        return pe;
    }

    @Transactional
    public ProductEntity closedProduct (String projectId, String adminSeq) {
        ProductEntity product = pr.findById(projectId)
                .orElseThrow(() -> new NotFound("상품을 찾을 수 없습니다: " + projectId));

        if (product.getProjectStatus() == ProductEntity.ProjectStatus.CLOSED) {
            throw new IllegalStateException("이미 CLOSED 상태입니다.");
        }
        if (product.getProjectStatus() != ProductEntity.ProjectStatus.DISTRIBUTING) {
            throw new IllegalStateException("END 상태가 아닌 상품은 CLOSED로 전환할 수 없습니다. 현재 상태=" + product.getProjectStatus());
        }
        product.setProjectStatus(ProductEntity.ProjectStatus.CLOSED);
        return pr.save(product);
    }
}
