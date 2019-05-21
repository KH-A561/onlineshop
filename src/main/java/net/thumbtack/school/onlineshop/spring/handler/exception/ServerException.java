package net.thumbtack.school.onlineshop.spring.handler.exception;

import net.thumbtack.school.onlineshop.spring.handler.exception.error.ErrorCode;


public class ServerException extends Exception {
    private ErrorCode errorCode;

    public ServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getErrorDescription();
    }
}
