package com.ddiring.BackEnd_Product.api;

import com.ddiring.BackEnd_Product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiProductService {

    private final ApiProductRepository apiProductRepository;

    public ListInvestmentDto getListInvestment(Integer productId) {
        Product product = apiProductRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException(""));

        return ListInvestmentDto.builder()
                .title(product.getTitle())
                .goalAmount(product.getGoalAmount())
                .endDate(product.getEndDate())
                .build();
    }
}
