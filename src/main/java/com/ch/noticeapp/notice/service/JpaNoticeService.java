package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.entity.Notice;
import com.ch.noticeapp.notice.exception.NoticeErrorCode;
import com.ch.noticeapp.notice.exception.NoticeException;
import com.ch.noticeapp.notice.repository.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor    // final이 붙은 필드만 모아서 생성자를 자동 생성
public class JpaNoticeService {
    @PersistenceContext
    private EntityManager entityManager;    // 쿼리에 즉시 반영 등을 처리할 수 있음. @PersistenceContext 을 붙여야 함.

    // JPA 는 JpaRepository 인터페이스를 통해 Entity 를 제어한다.
    // 또한 Entity 가 제어되면 자동으로 CRUD 가 수행됨. 따라서 개발자가 SQL 문을 볼 일이 없다.
    private final NoticeRepository noticeRepository;

    // 글 등록
    @Transactional
    public ResponseNotice regist(RequestNotice requestNotice) {
        Notice notice = new Notice(requestNotice.getTitle(), requestNotice.getWriter(), requestNotice.getContent());

        /* save() 메서드에 의해 DB에 즉시 쿼리문이 실행되는 것은 아님.
           notice 를 영속성(Persistence) 컨텍스트 관리 대상으로 등록한다는 의미(JPA 에 맡김)
           즉, INSERT 가 일어나는 시점은 현재 메서드가 완료되면서 트랜잭션이 확정될 때 이다.
        */
        Notice saved = noticeRepository.save(notice);  // 이러면 DB에 저장하고 저장된 게시물 반환
        entityManager.refresh(saved);   // flush 발생 -> INSERT 실행 -> SELECT 실행하여 DB 값을 재 로딩함.
        // !중요! - 절대 return 에 Entity를 그대로 반환해선 안 된다.
        // DB 와 관련된 정보, 객체간의 관계(erd 상 관계 등) 이 전부 들어가있으므로
        // Entity 안에 들어있는 데이터 중 필요한 것만 꺼내서 담을 DTO 가 필요하다. 그래서 Response용 Notice DTO 를 따로 정의한 것.

        // 특정 객체 안의 데이터를 다른 데이터로 옮길 때 생성자를 통해 옮기면 파라미터 순서도 중요하고
        // .getTitle() 로 하나하나 다 넣으면 코드가 복잡하고 번거로우니 빌더패턴(GOF) 를 사용.
        return ResponseNotice.from(notice);
    }


    // 글 목록 가져오기
    @Transactional(readOnly = true)  // readOnly 의 의미.
    // 엔터티를 수정하지 않을 것이라는 의도를 스프링에 명확히 하기 위함
    public List<ResponseNotice> getList() {
        // 모든 레코드 가져오기. 따로 정의한 메서드가 아니라 NoticeRepository 에 상속받은 JpaRepository 에서 지원하는 메서드.
        return noticeRepository.findAll(Sort.by(Sort.Direction.DESC, "noticeId"))
                .stream()  // 이 시점부터 컬렉션을 선언적 방식으로 처리할 수 있는 스트림을 시작한다는 의미.
                .map(ResponseNotice :: from)  // .map(notice -> ResponseNotice.from(notice)) 람다식 대신 :: 로 줄여쓸 수 있다.
                .toList();

        // findAll() 메서드로 가져온 결과를 우리가 원하는 형태인 List<ResponseNotice> 로 변환작업을 해야 하는데
        // 전통적인 방법으로는 반복문 돌려서 요소를 List 에 add 해야 하지만 자바스크립트의 배열메서드같은 java의 Stream 을 이용한다.
    }

    // 글 한 건 가져오기
    @Transactional
    public ResponseNotice getDetail(long noticeId) {    // 박싱 언박싱으로 이렇게 쓰나 Long 으로 쓰나 같음
        // findById() 메서드는 Optional 이 반환됨.
        // Optional 은 데이터가 들어있을 수도 있고, 없을 수도 있음을 객체로 표현한 것. 그래서 orElseThrow 같은 걸 붙여야한다. 그냥 쓰면 없을 수도 있으니까 오류뜸.
        Notice notice = noticeRepository.findById(noticeId).orElseThrow( ()-> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        notice.increaseHit();
        return ResponseNotice.from(notice);
    }

    // 글 한 건 수정
    @Transactional
    public ResponseNotice update(Long noticeId, RequestNotice requestNotice) {
        // 수정에 앞서, 수정 대상이 실제 존재하는지 여부를 먼저 따져보기
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));

        // 수정하기
        notice.update(requestNotice.getTitle(), requestNotice.getWriter(), requestNotice.getContent());
        // 개발자가 SQL 을 직접 건드는 게 아니라 이런식으로 Notice 엔터티를 수정하면 JPA 가 이 메서드 완료 시점에 MySQL 에 변경된 내용을 반영해줌.
        // JPA 의 철학: 개발자는 객체에 집중.

        return ResponseNotice.from(notice);
    }

    // 글 한 건 삭제
    @Transactional
    public void delete(long noticeId) { // 박싱 언박싱이라 Long 이든 long 이든 상관없음. DTO 는 Long 으로 되어있음
        // 이번에도 삭제하기 전에 실제 대상이 존재하는지 확인부터 먼저.
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(()-> new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND));
        noticeRepository.delete(notice);    // 삭제
    }

}











