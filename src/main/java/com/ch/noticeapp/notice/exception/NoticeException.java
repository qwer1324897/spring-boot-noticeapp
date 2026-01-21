package com.ch.noticeapp.notice.exception;

public class NoticeException extends RuntimeException{
    // 미리 만들어 놓은 예외 코드 객체(NoticeErrorCode enum) 사용하기
    private final NoticeErrorCode errorCode;

    public NoticeException(NoticeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public NoticeErrorCode getErrorCode() {
      return errorCode;
    }
}
