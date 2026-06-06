package com.samteo.global.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 성공 응답과 실패 응답을 동일한 형식으로 감싸기 위한 공통 API 응답 래퍼이다.
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;

    /**
     * 요청이 정상 처리된 경우 사용할 성공 응답을 생성한다.
     *
     * @param data 응답 데이터
     * @param <T> 응답 데이터 타입
     * @return 성공 응답 래퍼
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    /**
     * 요청 처리 중 오류가 발생한 경우 사용할 실패 응답을 생성한다.
     *
     * @param message 클라이언트에 전달할 오류 메시지
     * @param <T> 응답 데이터 타입
     * @return 실패 응답 래퍼
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
