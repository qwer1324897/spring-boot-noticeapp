package com.ch.noticeapp.notice.repository;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// Mapper 인터페이스를 사용하면, 굳이 DAO를 별도로 만들어서 SqlSessionTemplate 을 사용할 필요 없다.
@Mapper
public interface NoticeMapper {
    // 어차피 인터페이스에 선언된 메서드들은 디폴트가 public 이므로, public 을 명시하지 않아도 된다.
    int insert(Notice notice);  // 한 건 등록
    List<Notice> findAll(); // 목록 가져오기
    Notice findById(long noticeId); // 찾기
    int update(Notice noitce);  // 한 건 수정
    int deleteById(long noticeId);  // 한 건 삭제
    int increaseHit(long noticeId); // 조회수 증가
}
