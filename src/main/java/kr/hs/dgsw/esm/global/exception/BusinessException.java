package kr.hs.dgsw.esm.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int status;
    private final String code;

    public BusinessException(int status, String message, String code) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
