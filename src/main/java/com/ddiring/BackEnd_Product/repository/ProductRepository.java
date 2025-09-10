package com.ddiring.BackEnd_Product.repository;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends MongoRepository<ProductEntity, String> {
    // 1. 전체 조회수 높은순
    List<ProductEntity> findTop10ByOrderByViewCountDesc();
    // 2. 마감기일이 남은것 중 조회수 높은순
    List<ProductEntity> findTop10ByEndDateAfterOrderByViewCountDesc(LocalDateTime now);
    // 3. 모금액 높은순
    List<ProductEntity> findTop10ByOrderByAmountDesc();

    // 4. 유저가 즐겨찾기한 상품 목록
    @Query("{ 'favorites': ?0 }")
    List<ProductEntity> findByFavoritedUser(String userSeq);
    // 5. 특정 상품을 유저가 즐겨찾기했는지 존재 여부
    @Query(value = "{ '_id': ?0, 'favorites': ?1 }", exists = true)
    boolean existsByIdAndFavoritedUser(String projectId, String userSeq);

    // 6. 계좌번호로 상품 조회
    Optional<ProductEntity> findByAccount(String account);

    // 7. Public 조회
    List<ProductEntity> findAllByProjectVisibility(
            ProductEntity.ProjectVisibility visibility, Sort sort);

    // 8. 단일 조회 조회
    List<ProductEntity> findAllByProjectStatusAndProjectVisibility(
            List<ProductEntity.ProjectStatus> status,
            ProductEntity.ProjectVisibility visibility,
            Sort sort);

    // 9. List 조회 조회
    List<ProductEntity> findAllByProjectStatusInAndProjectVisibility(
            List<ProductEntity.ProjectStatus> status,
            ProductEntity.ProjectVisibility visibility,
            Sort sort);

    // 10. 창작자조회
    Optional<ProductEntity> findByUserSeq(String userSeq);
}
