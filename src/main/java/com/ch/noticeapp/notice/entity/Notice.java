package com.ch.noticeapp.notice.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
// Mybatis 는 자바 객체와 개발자가 정의해놓은 SQL 문을 자동으로 매핑해주는 SQLMapper 라면,
// JPA(Java Persistence Api) 는 자바 객체와 데이터베이스의 테이블 간 매핑을 자동으로 해주는 기술
// 자바 객체(Object) 관계형DB(Relation) 매핑(Mapping) => ORM JPA는 ORM 이다.

@Entity // 이 객체가 일반 클래스가 아닌 JAP 기술이 적용된 클래스임을 명시하는 애노테이션
           // 개발자가 쿼리문을 작성하는 것이 아니라, 이 객체에 데이터를 채워넣기만 하면, 알아서 내부적으로 insert 동작.
           // OOP 개발자는 쿼리문을 신경쓰지 말고, 객체간의 관계 등에 집중하면 된다.
@Table(name = "notice")
// JPA Entity 는 반드시 파라미터 없는 생성자가 존재해야 한다. 단, Entity 객체는 개발자가 임의로 인스턴스를 생성해선 안 된다.
// JPA 내부적으로 알아서 생성해주므로, 개발자에 의한 생성자 호출은 금지해야 한다. 따라서 public 이 아닌 protected 로 선언한다.
// protected 로 선언하면 JAP 및 상속 관계에 있는 객체들이 접근할 수 있게 된다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // protected Notice() {} 와 동일한 애노테이션
@Getter // @Setter 추가하지 않는 이유: DB 테이블과 연결되어 있으므로 쉽게 변경할 수 있는 Setter 등을 제공해선 안 된다.
// DTO 와는 아예 다르다. Entity 는 절대 클라이언트가 전송하는 파라미터를 받는 용도로 사용해선 안 된다.
// 컨트롤러 계층에서는 사용 X. 모델 영역에서만 사용
public class Notice {
    @Id // 이건 Id 임을 명시하는 애노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mysql 에 의해 값이 바뀐다는 걸 명시하는 애노테이션
    @Column(name = "notice_id")
    private Long noticeId;  // db의 칼럼명이 스네이크 케이스여도 Entity 는 캐멜 케이스로 작성.

    @Column(name = "title", length = 100)   // DB까지 깊숙히 들어가서 에러를 발생시키면 시간, 지원 낭비니까 여기서 검사.
    private String title;

    @Column(name = "writer", length = 20)
    private String writer;

    @Lob  // Large Object 의 약자로서, @Lob 애노테이션을 String 에 붙이면 CLOB, byte[] 에 붙이면 BLOB 계열로 매핑
    // MySQL 의 경우 보통 String 은 디폴트로 varchar(255) 로 가려는 경향이 있으므로, columnDefinition="text" 를 명시함으로서
    // 컬럼의 타입을 TEXT 로 정확히 알려주기 위함. 현재 지금의 경우 MySQL에 text 로 이미 테이블을 등록해놨기 때문에 큰 의미는 없지만,
    // 만약 협업을 할 경우, 다른 개발자와 프로젝트를 공유할 때 이 속성을 보고 용량이 큰 스트림 데이터임을 문서화.
    @Column(name = "content", columnDefinition = "text")
    private String content;

    // String 이 아닌 날짜 자료형으로 처리하면 정렬, 날짜비교, 명확한 기간조회, 유효한 날짜만 허용 등 문자열 오염을 방지한다.
    @Column(name = "regdate", insertable = false, updatable = false)
    // 날짜의 경우 개발자가 따로 핸들링하는 게 아니라 DB에서 알아서 하므로, insertable, updatable 을 false 로 명시.
    private LocalDateTime regdate;

    @Column(name = "hit")
    private int hit;    // Integer 를 쓰는 이유는 null 값 비교를 할 수 있으므로. 지금의 경우 int 를 써도 아무상관없음


    // 개발자가 추후 데이터 1건을 담기 위해 사용될 생성자 정의
    public Notice(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public void update(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    public void increaseHit() {
        hit += 1;
    }
}











