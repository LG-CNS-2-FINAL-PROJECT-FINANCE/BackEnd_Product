package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.kafka.NotificationProducer;
import com.ddiring.BackEnd_Product.kafka.enums.NotificationType;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository pr;
    private final MongoTemplate mt;
    private final NotificationProducer notificationProducer;

    /* ---------- 투자 가능한 모든 상품 조회 ---------- */
    public List<ProductListDto> getAllOpenProject() {
        return pr.findAllByProjectStatusAndProjectVisibility(
                List.of(
                        ProductEntity.ProjectStatus.OPEN,
                        ProductEntity.ProjectStatus.FUNDING_LOCKED
                        ),
                        ProductEntity.ProjectVisibility.PUBLIC,
                        Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ProductListDto::from)
                .toList();
    }

    /* ---------- 투자 불가능한 모든 상품 조회 ---------- */
    public List<ProductListDto> getAllUnOpenProject() {
        return pr.findAllByProjectStatusInAndProjectVisibility(
                List.of(
                        ProductEntity.ProjectStatus.TRADING,
                        ProductEntity.ProjectStatus.DISTRIBUTION_READY,
                        ProductEntity.ProjectStatus.DISTRIBUTING,
                        ProductEntity.ProjectStatus.CLOSED
                ),
                        ProductEntity.ProjectVisibility.PUBLIC,
                        Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ProductListDto::from)
                .toList();
    }

    /* ---------- 상품 상세 조회 ---------- */
    public ProductDetailDto getProjectId(String projectId, String userSeq) {
        viewCount(projectId); // 조회수 증가
        ProductEntity product = pr.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
        return ProductDetailDto.from(product, userSeq);
    }

    /* ---------- 모든 상품 조회 (관리자) ---------- */
    public List<ProductListDto> getAllProjectAdmin() {
        return pr.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ProductListDto::from)
                .toList();
    }

    /* ---------- 상품 상세 조회 (관리자) ---------- */
    public ProductDetailDto getProjectIdAdmin(String projectId, String adminSeq) {
        ProductEntity pe = pr.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
        return ProductDetailDto.from(pe, adminSeq);
    }

    /* ---------- 상품 조회수 증가 ---------- */
    public void viewCount(String projectId) {
        mt.getCollection("product") // 실제 컬렉션 이름
                .updateOne(
                        new Document("_id", projectId),
                        new Document("$inc", new Document("viewCount", 1))  
                );
    }

    /* ---------- 상품 종료 처리 ---------- */
    @Transactional
    public void  closedProduct (String projectId, String adminSeq) {
        ProductEntity pe = pr.findById(projectId)
                .orElseThrow(() -> new NotFound("상품을 찾을 수 없습니다: " + projectId));

        if (pe.getProjectStatus() == ProductEntity.ProjectStatus.CLOSED) {
            throw new IllegalStateException("이미 CLOSED 상태입니다.");
        }
        if (pe.getProjectStatus() != ProductEntity.ProjectStatus.DISTRIBUTING) {
            throw new IllegalStateException("DISTRIBUTION 상태가 아닌 상품은 CLOSED로 전환할 수 없습니다. 현재 상태=" + pe.getProjectStatus());
        }
        pe.setProjectStatus(ProductEntity.ProjectStatus.CLOSED);

        pe.setClosedAdminSeq(adminSeq);
        pr.save(pe);

        // 🔔 분배금 입금 확인(창작자)
        notificationProducer.sendNotification(
                List.of(pe.getUserSeq()),
                NotificationType.INFORMATION.name(),
                "상품 종료",
                "상품(" + pe.getTitle() + ")의 모든 절차가 마무리되어 종료되었습니다."
        );
    }
}
