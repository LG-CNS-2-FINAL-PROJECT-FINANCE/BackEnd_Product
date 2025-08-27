package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void cancelRequest(String requestId, String userSeq) {
        ProductRequestEntity pre = prr.findById(requestId)
                .orElseThrow(() -> new NotFound("요청이 없습니다: " + requestId));

        // 본인 요청인지 확인
        if (!pre.getUserSeq().equals(userSeq)) {
            throw new ForbiddenException("본인 요청만 취소할 수 있습니다.");
        }

        // 이미 처리된 요청은 취소 불가
        if (pre.getStatus() != ProductRequestEntity.RequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청은 취소할 수 없습니다.");
        }

        // 그냥 DB에서 삭제
        prr.delete(pre);
    }
}
