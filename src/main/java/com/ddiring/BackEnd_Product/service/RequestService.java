package com.ddiring.BackEnd_Product.service;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import com.ddiring.BackEnd_Product.common.exception.NotFound;
import com.ddiring.BackEnd_Product.dto.request.RequestDetailDto;
import com.ddiring.BackEnd_Product.dto.request.RequestListDto;
import com.ddiring.BackEnd_Product.entity.ProductRequestEntity;
import com.ddiring.BackEnd_Product.repository.ProductRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ProductRequestRepository prr;

    /* ---------- 모든 요청 조회 ---------- */
    public List<RequestListDto> getAllRequest() {
        return prr.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(RequestListDto::from)
                .toList();
    }

    /* ---------- 요청 상세 조회 ---------- */
    public RequestDetailDto getRequestByRequestId(String requestId) {
        ProductRequestEntity request = prr.findById(requestId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 요청입니다."));
        return RequestDetailDto.from(request);
    }

    /* ---------- 요청 취소 ---------- */
    @Transactional
    public void cancelRequest(String requestId, String userSeq) {
        ProductRequestEntity pre = prr.findById(requestId)
                .orElseThrow(() -> new NotFound("요청이 없습니다: " + requestId));

        if (!pre.getUserSeq().equals(userSeq)) {
            throw new ForbiddenException("본인 요청만 취소할 수 있습니다.");
        }

        if (pre.getRequestStatus() != ProductRequestEntity.RequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청은 취소할 수 없습니다.");
        }

        prr.delete(pre); // DB 삭제
    }

    /* ---------- 모금액 퍼센트 계산 ---------- */
    @Transactional
    public BigDecimal DistributionPercent(BigDecimal distributionAmount, BigDecimal goalAmount) {
        if (distributionAmount == null || goalAmount == null || goalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal percent = distributionAmount
                .divide(goalAmount, 4, RoundingMode.HALF_UP) // 소수점 넉넉히
                .multiply(new BigDecimal("100"))             // %
                .min(new BigDecimal("100"));                 // 100% 이상 방지

        return percent.setScale(1, RoundingMode.HALF_UP);    // 소수점 1자리까지
    }
}
