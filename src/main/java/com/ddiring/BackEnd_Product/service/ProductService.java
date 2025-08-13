package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.asset.AssetRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.external.AssetClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
public class ProductService {

    private final ProductRepository pr;
    private final MongoTemplate mt;
    private final AssetClient ac;

    public List<ProductListDto> getAllProduct() {
        return pr.findAll().stream()
                .map(ProductListDto::from)
                .collect(Collectors.toList());
    }

    public ProductDetailDto getProductByProductId(String productId) {
        viewCount(productId);
        ProductEntity product = pr.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
        return ProductDetailDto.from(product);
    }

    public void viewCount(String productId) {
        mt.getCollection("product") // 실제 컬렉션 이름
                .updateOne(
                        new Document("_id", productId),
                        new Document("$inc", new Document("viewCount", 1))  
                );
    }

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
                    && pe.getStatus() == ProductEntity.ProductStatus.OPEN) {
                pe.setStatus(ProductEntity.ProductStatus.END);
            }

            pr.save(pe);
        }
    }

    @Transactional
    public ProductEntity sendAsset(AssetRequestDto dto) {
        if (dto == null || dto.getProjectId() == null) {
            throw new IllegalArgumentException("projectId가 필요합니다.");
        }

        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        // DB 내용으로 최종 DTO를 확정하고 싶으면 여기서 dto 보정 가능
        // (예: dto.setTitle(pe.getTitle());)
        ac.asset(dto); // 동기 호출
        return pe;
    }
}
