package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import com.ch.noticeapp.notice.service.MybatisNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

// 공지게시판에 요청을 처리하는 컨트롤러
@RestController // 이 애노테이션을 쓰면 @ResponseBody 애노테이션을 붙일 필요 없다.
@Slf4j
@RequiredArgsConstructor    // final이 붙은 필드만 모아서 생성자를 자동 생성
// @RequestMapping("/api/notices")
public class MybatisNoticeController {

    private final MybatisNoticeService noticeService;   // @Autowired 대신, final 로 초기화 후 생성자를 강제.(실수방지)

    // 글쓰기 요청 처리
    @PostMapping
    // @ResponseBody   @ResponseBody 애노테이션: jackson 라이브러리가 java 클래스를 json 문자열로 바꿔줌
    public ResponseEntity<Notice> regist(@RequestBody Notice notice) {  // @RequestBody 애노테이션: jackson 라이브러리가 json 문자열을 java로 자동 매핑 해준다.
        log.debug("넘겨받은 제목: " + notice.getTitle());

        Notice created = noticeService.regist(notice);

        // created() 메서드 안에는 클라이언트에게 등록된 자원의 위치를 알려주는 코드를 작성할 수 있음
        // return ResponseEntity.created(URI.create("/api/notices/" + notice.getNoticeId())).body(notice);
        return ResponseEntity.created(URI.create("/api/notices/" + created.getNoticeId())).body(created);
    }

    // 글 삭제
    @DeleteMapping("/{noticeId}")
    // 변수가 경로의 일부로 포함될 경우, {중괄호} 를 붙이고 @PathVariable 애노테이션을 사용하면 경로의 일부로 보는 게 아니라 변수로 인식한다.
    public ResponseEntity<Void> delete(@PathVariable Long noticeId) {
        log.debug("삭제한 글의 pk 값은: " + noticeId);
        noticeService.delete(noticeId);
        return ResponseEntity.noContent().build();
    }

    // 글 한 건 가져오기
    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getContent(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getContent(noticeId));
    }

    // 글 목록 가져오기
    @GetMapping
    public ResponseEntity<List<Notice>> getList() { // <List<Notice>> 이렇게 써야 json 으로 변환.
        return ResponseEntity.ok(noticeService.getList());
    }

    // 글 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<Notice> update(@PathVariable Long noticeId, @RequestBody Notice notice) {
        log.debug("수정된 notice 는: " + notice);
        notice.setNoticeId(noticeId);   // pk 값 대입

        return ResponseEntity.ok(noticeService.update(notice));
    }


}