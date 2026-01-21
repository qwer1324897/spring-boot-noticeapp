package com.ch.noticeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @GetMapping("/test")
    @ResponseBody   // Spring Boot 는 REST API 기반이기 때문에, MessageConverter 등록이나 jackson 라이브러리를 별도로 추가할 필요가 없다.
    public String getMsg() {
        return "Hello World";
    }
}
