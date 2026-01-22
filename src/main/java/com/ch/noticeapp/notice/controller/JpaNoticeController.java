package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.service.JpaNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController   // 이 애노테이션을 붙이면 모든 요청 처리 메서드에 @ResponseBody 애노테이션을 붙일 필요가 없다
@RequestMapping("api/notices")
@RequiredArgsConstructor
public class JpaNoticeController {

    private final JpaNoticeService jpaNoticeService;

    @PostMapping
    public ResponseEntity<ResponseNotice> regist(@RequestBody RequestNotice requestNotice) {
        // 클라이언트가 전송한 파라미터가 json 문자열이므로, @RequestBody 애노테이션을 붙여서
        // json 문자열을 자바 객체와 자동으로 매핑을 시켜주는 jackson을 사용할 수 있다.
        ResponseNotice created = jpaNoticeService.regist(requestNotice);
        return ResponseEntity.ok(created);
    }

    // 목록 요청 처리
    @GetMapping
    public ResponseEntity<List<ResponseNotice>> getList() {
        return ResponseEntity.ok(jpaNoticeService.getList());
    }

    // 수정 요청 처리
    @PutMapping("/{noticeId}")
    public ResponseEntity<ResponseNotice> update(@PathVariable Long noticeId, @RequestBody RequestNotice requestNotice) {
        return ResponseEntity.ok(jpaNoticeService.update(noticeId, requestNotice));
    }

    // 삭제 요청 처리
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> delete(@PathVariable Long noticeId) {  // 삭제는 반환할 게 없으니 제너릭을 줄 필요 없지만 <Void> 를 주는 이유는
        // @RestController 애노테이션에 의해 @ResponseBody 애노테이션이 사실상 다 붙어있는데, 이 애노테이션은 json으로 변환을 해주는데 제너릭을 안 쓰면 변환대상이 없으므로
        // Void 를 써서 객체임을 명시.
        jpaNoticeService.delete(noticeId);
        return ResponseEntity.noContent().build();  // 204 no content
    }

}
