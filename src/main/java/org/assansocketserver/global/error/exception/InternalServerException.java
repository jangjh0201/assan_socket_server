package org.assansocketserver.global.error.exception;

import org.assansocketserver.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}