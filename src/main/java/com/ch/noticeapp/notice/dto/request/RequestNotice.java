package com.ch.noticeapp.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
// JPA 이용 시에는 Setter 를 명시하지 않아도 된다. JPA 가 자바의 리플렉션의 원리를 이용한다.
// Field field = NoticeRequest.class.getDeclaredField("title");
// field.set("title"); 이런식으로 자바 스탠다드에 있는 기능으로 주입해줌
public class RequestNotice {

    @NotBlank
    @Size(max = 100)
    // @NotBlank - null, 빈 문자열("" ), 공백만 있는 문자열(" ") 등을 허용하지 않음. db까지 가기 전에 유효성을 체크해주는 애노테이션
    // @Size - 문자열, 컬렉션, 배열의 길이를 검증. Spring의 경우 문자열 길이에 대해 기준치 초과 여부를 판단해주는 애노테이션
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;
}
