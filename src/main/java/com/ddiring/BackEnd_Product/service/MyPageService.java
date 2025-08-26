package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MongoTemplate mt;

    public Page<RequestListDto> getMyRequest(String userSeq, Pageable p) {

    }
}
