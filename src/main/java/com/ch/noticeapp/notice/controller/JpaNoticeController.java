package com.ch.noticeapp.notice.controller;

import com.ch.noticeapp.notice.dto.request.RequestNotice;
import com.ch.noticeapp.notice.dto.response.ResponseNotice;
import com.ch.noticeapp.notice.service.JpaNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
