package com.samteo.domain.health.service;

import org.springframework.stereotype.Service;

/**
 * 모니터링이나 스모크 테스트에서 사용할 최소한의 헬스체크 상태를 제공한다.
 */
@Service
public class HealthService {

    /**
     * 애플리케이션이 정상 구동 중일 때 고정된 상태 메시지를 반환한다.
     *
     * @return 헬스체크 메시지
     */
    public String getStatus() {
        return "Samteo server is running.";
    }
}
