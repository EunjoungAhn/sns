package com.fastcampus.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// application에서 따로 사용할 Exception
@Getter
@AllArgsConstructor
public class SnsApplicationException extends RuntimeException{

    private ErrorCode errorCode;
    private String message;

    //메시지 없이 에러띄우는 생성자 생성
    public SnsApplicationException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if(message == null){
            return errorCode.getMessage();
        }

        return String.format("%s. %s", errorCode.getMessage(), message);
    }
}
