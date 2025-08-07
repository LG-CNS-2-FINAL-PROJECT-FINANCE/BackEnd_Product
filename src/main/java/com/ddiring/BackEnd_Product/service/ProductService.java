package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.escrow.AccountRequestDto;
import com.ddiring.BackEnd_Product.dto.escrow.AccountResponseDto;
import com.ddiring.BackEnd_Product.dto.escrow.AmountDto;
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

    public void syncAccount(String productId) {

        AccountRequestDto request = AccountRequestDto.builder()
                .productId(productId)
                .build();

        AccountResponseDto response = ec.createAccount(request);

        ProductEntity product = pr.findById(productId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        product.setAccount(response.getAccount());
        pr.save(product);
    }

    public void syncAmount(AmountDto dto) {
        ProductEntity p = pr.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

        if (dto.getAmount().compareTo(p.getAmount()) != 0) {
            p.setAmount(dto.getAmount());
            pr.save(p);
        }
    }
}
