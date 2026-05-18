package kr.hs.dgsw.esm.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String message;
    private final String code;

    public static ErrorResponse of(int status, String message, String code) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .code(code)
                .build();
    }
}
