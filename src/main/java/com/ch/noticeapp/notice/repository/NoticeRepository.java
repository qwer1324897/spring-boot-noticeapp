package com.ch.noticeapp.notice.repository;

import com.ch.noticeapp.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

// 쿼리문 없이 객체를 제어하여 간접적으로 DB 와 CRUD 를 수행
public interface NoticeRepository extends JpaRepository<Notice, Long> { //<엔터티, pk자료형>

}
