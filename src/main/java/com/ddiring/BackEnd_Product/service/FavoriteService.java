package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.kafka.NotificationProducer;
import com.ddiring.BackEnd_Product.kafka.enums.NotificationType;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final MongoTemplate mt;
    private final ProductRepository pr;
    private final NotificationProducer notificationProducer;

    /** 토글: 추가 시 true, 제거 시 false 반환 */
    public boolean toggle(String projectId, String userSeq) {
        // 1) addToSet 시도
        Query q = Query.query(Criteria.where("_id").is(projectId));
        Update add = new Update().addToSet("favorites", userSeq);
        UpdateResult added = mt.updateFirst(q, add, ProductEntity.class);

        if (added.getMatchedCount() == 0) {
            throw new IllegalArgumentException("상품이 없습니다: " + projectId);
        }
        // 🔔 modifiedCount==1 이면 방금 추가됨 → true
        if (added.getModifiedCount() == 1) {
            // 상품 정보 조회
            ProductEntity pe = mt.findById(projectId, ProductEntity.class);
            if (pe == null) {
                throw new IllegalArgumentException("상품이 없습니다: " + projectId);
            }

            // 투자자 알림 발행
            notificationProducer.sendNotification(
                    List.of(userSeq),
                    NotificationType.INFORMATION.name(),
                    "즐겨찾기 추가",
                    "\"" + pe.getTitle()+ "\" 상품이 즐겨찾기를 추가되었습니다."
            );

            // 장착자 알림 발행
            notificationProducer.sendNotification(
                    List.of(pe.getNickname()),
                    NotificationType.INFORMATION.name(),
                    "즐겨찾기 누름",
                    "\"" + pe.getTitle()+"\"에 "+pe.getNickname()+"님이 즐겨찾기를 눌렀습니다."
            );
            return true;
        }
        // 2) 이미 있었던 경우 → pull로 제거 → false
        Update pull = new Update().pull("favorites", userSeq);
        mt.updateFirst(q, pull, ProductEntity.class);
        return false;
    }

    /** 유저가 즐겨찾기한 상품 목록 */
    public List<ProductEntity> listByUser(String userSeq) {
        return pr.findByFavoritedUser(userSeq);
    }

    // 필요하면 사용
    /** 특정 상품에 대해 유저가 즐겨찾기 했는지 */
    public boolean isFavorited(String projectId, String userSeq) {
        return pr.existsByIdAndFavoritedUser(projectId, userSeq);
    }
}

