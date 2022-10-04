package com.fastcampus.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// application에서 따로 사용할 Exception
@Getter
@AllArgsConstructor
public class SnsApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    @Override
    public String getMessage() {
        if(message == null){
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode, getMessage(), message);
    }
}
