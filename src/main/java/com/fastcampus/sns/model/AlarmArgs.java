package com.fastcampus.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmArgs {
    // 알림을 발생시킨 사람
    private Integer fromUserId;
    private Integer targetId;
}
