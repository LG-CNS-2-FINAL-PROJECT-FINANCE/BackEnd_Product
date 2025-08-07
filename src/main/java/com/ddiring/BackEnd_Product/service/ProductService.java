package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.product.escrow.AmountDto;
import com.ddiring.BackEnd_Product.dto.product.ProductDetailDto;
import com.ddiring.BackEnd_Product.dto.product.ProductListDto;
import com.ddiring.BackEnd_Product.entity.ProductEntity;
import com.ddiring.BackEnd_Product.external.EscrowClient;
import com.ddiring.BackEnd_Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository pr;
    private final MongoTemplate mt;
    private final EscrowClient ec;

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

    public void syncAmount(AmountDto dto) {
        ProductEntity pe = pr.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));
        pe.setAmount(dto.getBalance());
        pr.save(pe);
    }
}
