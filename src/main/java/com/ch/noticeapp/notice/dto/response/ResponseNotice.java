package com.ch.noticeapp.notice.dto.response;

import com.ch.noticeapp.notice.entity.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// JPA 기반으로 개발을 하게 될 경우 테이블과 1:1 매핑되는 Entity 클래스가 필요하고,
// 이 클래스는 DTO 가 아니지만 이미 우리가 Notice.java 라는 이름으로 평상시 DTO로 써먹는 이름을
// Entity 가 점유하였으므로, 응답용 DTO 를 정의하자.
@Builder
@Getter
public class ResponseNotice {
    private Long noticeId;
    private String title;
    private String writer;
    private String content;
    private Integer hit;
    private LocalDateTime regdate;

    // 빌더 패턴으로 메서드 정의할 예정
    // 빌더 패턴을 사용하지 않을 경우, Notice 안에 있는 데이터들을 생성자를 이용하여 옮길 때
    // 순서를 맞춰야 하므로 코드가 매우 복잡해진다.
    public static ResponseNotice from(Notice notice) {
        return ResponseNotice.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .writer(notice.getWriter())
                .content(notice.getContent())
                .regdate(notice.getRegdate())
                .hit(notice.getHit())
                .build();
    }
}
