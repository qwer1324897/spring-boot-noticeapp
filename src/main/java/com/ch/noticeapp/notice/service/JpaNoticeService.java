package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.entity.Notice;
import com.ch.noticeapp.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor    // final이 붙은 필드만 모아서 생성자를 자동 생성
public class JpaNoticeService {
    // JPA 는 JpaRepository 인터페이스를 통해 Entity 를 제어한다.
    // 또한 Entity 가 제어되면 자동으로 CRUD 가 수행됨. 따라서 개발자가 SQL 문을 볼 일이 없다.
    private final NoticeRepository noticeRepository;

    // 글 등록
    @Transactional
    public ResponseNotice regist(RequestNotice requestNotice) {
        Notice notice = new Notice(requestNotice.getTitle(), requestNotice.getWriter(), requestNotice.getContent());
        Notice saved = noticeRepository.save(notice);  // 이러면 DB에 저장하고 저장된 게시물 반환
        // !중요! - 절대 return 에 Entity를 그대로 반환해선 안 된다.
        // DB 와 관련된 정보, 객체간의 관계(erd 상 관계 등) 이 전부 들어가있으므로
        // Entity 안에 들어있는 데이터 중 필요한 것만 꺼내서 담을 DTO 가 필요하다. 그래서 Response용 Notice DTO 를 따로 정의한 것.

        // 특정 객체 안의 데이터를 다른 데이터로 옮길 때 생성자를 통해 옮기면 파라미터 순서도 중요하고
        // .getTitle() 로 하나하나 다 넣으면 코드가 복잡하고 번거로우니 빌더패턴(GOF) 를 사용.
        return ResponseNotice.from(notice);
    }
}











