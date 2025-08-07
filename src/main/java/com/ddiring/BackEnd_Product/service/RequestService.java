package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ProductRequestRepository prr;

    public List<RequestListDto> getAllRequest() {
        return prr.findAll().stream()
                .map(RequestListDto::from)
                .collect(Collectors.toList());
    }

    public RequestDetailDto getRequestByRequestId(String requestId) {
        ProductRequestEntity request = prr.findById(requestId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));
        return RequestDetailDto.from(request);
    }
}
