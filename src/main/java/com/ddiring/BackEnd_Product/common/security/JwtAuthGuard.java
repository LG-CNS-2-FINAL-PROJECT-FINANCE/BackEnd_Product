package com.ddiring.BackEnd_Product.common.security;

import com.ddiring.BackEnd_Product.common.exception.ForbiddenException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JWT 기반 인증/인가 "가드" 컴포넌트.
 *
 * 역할:
 *  - Authorization 헤더(Bearer 토큰)에서 JWT를 꺼내고 검증
 *  - 클레임(특히 role, userSeq)을 읽어 권한 확인/사용자 식별
 *  - 컨트롤러에서 중복되는 인증/인가 코드를 한 곳으로 모아 재사용
 *
 * 전제:
 *  - JwtUtil.parseClaims(...)가 HS256 + (Base64 디코딩된 시크릿) 규칙으로 파싱하도록 구현되어 있어야 함
 *  - 토큰에 최소한 "role", "userSeq" 클레임이 들어 있어야 함
 */
@Component
@RequiredArgsConstructor
public class JwtAuthGuard {

    private final JwtUtil jwtUtil;

    /**
     * Authorization 헤더에서 JWT 클레임을 꺼낸다.
     *
     * 처리 순서:
     *  1) 헤더 존재 여부 확인 (없으면 403)
     *  2) "Bearer " 접두 형식 확인 (대/소문자 무시) – 아니면 403
     *  3) JwtUtil로 파싱 시도 – 실패 시 403
     *
     * 주의:
     *  - 현재 코드는 jwtUtil.parseClaims(h)로 "헤더 전체"를 넘긴다.
     *  - JwtUtil 내부가 "Bearer " 접두어 제거를 지원해야 정상 동작.
     *  - 만약 JwtUtil이 "순수 토큰"만 받는다면, 여기서 substring(7)로 토큰만 추출해서 넘겨야 한다.
     */
    public Claims requireClaims(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new ForbiddenException("권한 없음 (토큰 누락)");
        }
        String h = authorizationHeader.trim();
        if (!h.regionMatches(true, 0, "Bearer ", 0, 7)) { // 대/소문자 무시, 공백 포함
            throw new ForbiddenException("권한 없음 (Bearer 형식 아님)");
        }
        try {
            // JwtUtil.parseClaims 가 "Bearer " 접두 제거를 지원한다는 가정.
            // 만약 지원하지 않으면 -> jwtUtil.parseClaims(h.substring(7).trim());
            return jwtUtil.parseClaims(h);
        } catch (Exception e) {
            // 서명 불일치/만료/포맷 오류 등 상세 원인은 노출하지 않고 403으로 통일
            throw new ForbiddenException("권한 없음 (토큰 검증 실패)");
        }
    }

    /**
     * 주어진 Claims의 role 클레임에 required 중 하나라도 포함되어 있는지 검사.
     *
     * 예)
     *  requireAnyRole(claims, "ADMIN")         // ADMIN 필요
     *  requireAnyRole(claims, "CREATOR","ADMIN") // CREATOR 또는 ADMIN 허용
     */
    public void requireAnyRole(Claims c, String... required) {
        if (!hasAnyRole(c.get("role"), required)) {
            throw new ForbiddenException("권한 없음 (required=" + Arrays.toString(required) + ")");
        }
    }

    /**
     * JWT에서 userSeq 클레임을 필수로 추출.
     *
     * FIX 포인트:
     *  - String.valueOf(null)은 문자열 "null"을 반환해서, null/blank 체크를 통과해버리는 함정이 있음.
     *  - 안전하게 Object로 먼저 받고, null이면 바로 차단.
     */
    public String requireUserSeq(Claims c) {
        Object raw = c.get("userSeq");                // ★ 먼저 Object로 받기
        String s = (raw == null) ? null : String.valueOf(raw).trim();
        if (s == null || s.isBlank()) {
            throw new ForbiddenException("권한 없음 (userSeq claim 누락)");
        }
        return s;
    }

    /* =========================
       내부 유틸리티 메서드 모음
       ========================= */

    /** ROLE_ 접두어 제거 (ROLE_ADMIN -> ADMIN) */
    private static String stripRolePrefix(String r) {
        return r != null && r.startsWith("ROLE_") ? r.substring(5) : r;
    }

    /** 역할 문자열 표준화: 접두 제거 + 대문자 */
    private static String norm(String r) {
        return stripRolePrefix(String.valueOf(r)).toUpperCase(Locale.ROOT);
    }

    /**
     * role 클레임이 어떤 형태로 와도(required 중 하나라도 있으면 true):
     *  - 문자열: "ADMIN", "ADMIN,CREATOR", "[\"ADMIN\",\"CREATOR\"]"
     *  - 배열/컬렉션: ["ADMIN", "CREATOR"]
     *
     * 처리:
     *  1) 컬렉션이면 각 요소를 문자열화 → 표준화(norm) → Set에 담음
     *  2) 문자열이면 JSON 배열 문자열/CSV 양쪽 대응:
     *     - 양끝 대괄호/따옴표 제거 후 쉼표/세미콜론/스페이스로 split
     *  3) required 중 하나라도 포함되면 true
     */
    private boolean hasAnyRole(Object claim, String... req) {
        if (claim == null) return false;

        Set<String> have = new HashSet<>();

        if (claim instanceof Collection<?> col) {
            for (Object o : col) {
                have.add(norm(String.valueOf(o)));
            }
        } else {
            String raw = String.valueOf(claim).trim();
            // ["ADMIN","CREATOR"] 같은 JSON 배열 문자열일 때 따옴표/대괄호 제거
            if (raw.startsWith("[") && raw.endsWith("]")) {
                raw = raw.substring(1, raw.length() - 1)
                        .replace("\"", "")
                        .replace("'", "");
            }
            // 쉼표/세미콜론/공백 구분자 모두 허용
            for (String s : raw.split("[,;\\s]+")) {
                if (!s.isBlank()) {
                    have.add(norm(s));
                }
            }
        }

        // required 중 하나라도 보유하면 통과
        for (String r : req) {
            if (have.contains(norm(r))) return true;
        }
        return false;
    }
}
