package com.ch.noticeapp.notice.dto.mybatis;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 기존에 썼던 @Data는 많은 애노테이션을 자동으로 붙여주므로, 필요한 것만 설정.
// @Data 는 @Getter + @Setter + @ToString + @EnqualsAndHashCode + @RequiredArgsConstructor 등 많은 게 붙음

@Getter
@Setter
public class Notice {
    private Long noticeId; // long 이 아닌 객체 Long 을 쓰는 이유: null 값을 판단하기에 유리. 그냥 long 은 0 이 있으므로 null 보다는 의미가 약함.
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regdate;  // 단순 문자열로 날짜 자료형을 처리하면, 불러오기만 할 땐 문제 없지만 비교 및 형식평가에 불리함.
    private int hit;
}
