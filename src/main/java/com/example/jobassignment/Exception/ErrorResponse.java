package com.example.jobassignment.Exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorDetail error;

    @Getter
    @AllArgsConstructor
    @Schema(description = "에러 응답")
    public static class ErrorDetail {

        @Schema(description = "에러 코드", example = "에러 코드 예시입니다.")
        private String code;

        @Schema(description = "에러 메시지", example = "에러 메시지 예시입니다.")
        private String message;
    }

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(new ErrorDetail(code, message));
    }
}
