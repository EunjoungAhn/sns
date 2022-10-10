package com.fastcampus.sns.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    // 메세지의 변화가 있는 알람 같은 경우는 서버에서 관리해 주는 것이 좋다.
    // DB에서 관리하면 매번 꺼내서 바꾸어야 하기 때문이다.
    NEW_COMMENT_ON_POST("new comment!"),
    NEW_LIKE_ON_POST("new like!"),
    ;

    private final String alarmText;
}
