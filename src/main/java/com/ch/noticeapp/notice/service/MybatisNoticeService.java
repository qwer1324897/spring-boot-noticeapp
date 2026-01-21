package com.ch.noticeapp.notice.service;

import com.ch.noticeapp.notice.dto.mybatis.Notice;
import com.ch.noticeapp.notice.exception.NoticeErrorCode;
import com.ch.noticeapp.notice.exception.NoticeException;
import com.ch.noticeapp.notice.repository.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MybatisNoticeService {

    private final NoticeMapper noticeMapper;

    // Spring Boot 에선 @Autowired 보다 이렇게 생성자 주입 방식을 권고한다.
    // 서비스 객체가 반드시 필요로하는 객체의 주입을 강제함으로서 실수를 방지하기 위함
    // @RequiredArgsConstructor 애노테이션 (final이 붙은 필드만 모아서 생성자를 자동으로 만들어줌) 을 사용해도 된다.
    public MybatisNoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    // 글 등록
    // 이 메서드 안에서 throw 로 인한 throws 처리를 메서드 정의부에 해야 할까?
    // 해도 되고 안 해도 된다.
    // 할 경우 - 메서드의 정의부가 지저분해지고, 이 서비스의 상위 객체인 인터페이스를 정의할 경우 해당 메서드에도 throws 를 명시해야 하므로 코드량이 많아짐
    // 안 할 경우 - throw 를 만나면 반드시 throws 를 해야하는 건 아니다. 자바 내부적으로 throw 가 발생해도, 프로그램이 종료되지 않고 예외를 메서드 호출부로 전달해준다.
    // throw 를 명시했을 때 throws 를 해야하는 경우는 이전의 SQLException 에서나 사용했던 방법이므로,
    // 우리의 경우 RunTimeException 을 상속받은 예외 객체를 사용하고 있으므로, 굳이 throws 를 명시할 필요 없다.
    // 즉, SQLException 의 경우에는 처리를 강요하는 강제 Exception 이었기 때문에 throws 가 필요했단 것.
    public Notice regist(Notice notice) {
        // insert 후 반환되는 숫자는 레코드가 반영되었는지 여부를 판단할 때 사용하고,
        // 그 외 문법상 오류, db의 문제로 인한 외부적 에러는 예외 발생으로 판단해야 한다.
        try {
            int affected = noticeMapper.insert(notice);

            if (affected != 1) {
                throw new NoticeException(NoticeErrorCode.NOTICE_CREATE_FAIL);
            }
            return noticeMapper.findById(notice.getNoticeId());
            // return notice;
            // 이 시점. insert 가 완료된 시점의 notice DTO 에는 useGeneratedKeys 속성에 의해
            // pk 값이 채워진 상태이므로, 어떤 글을 넣었는지에 대한 정보를 클라이언트에게 전송할 수 있다.
        } catch (NoticeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoticeException(NoticeErrorCode.NOTICE_CREATE_FAIL);
        }
    }


    // 글 삭제
    public void delete(long noticeId) {

        try {
            int affeted = noticeMapper.deleteById(noticeId);
            if(affeted != 1) {  // 반영이 안 되었다면, 일종의 예외로 처리.
                throw new NoticeException(NoticeErrorCode.NOTICE_DELETE_FAIL);
            }
        } catch (NoticeException e) {
            // 이 catch 영역은 필수는 아니지만, 개발자가 예외시 전달 말고 다른 걸 더 하고 싶으면 이 영역을 사용할 수 있다.
            e.printStackTrace();
            throw e;
        } catch (Exception e) { // 이 곳에 오는 exception 은 예외의 종류가 너무 많고 예측이 불가하므로 그냥 Exception 형으로 받아야 하고
                                        // 글 삭제의 모든 예외는 모두 NoticeException 으로 몰아서 처리.
            throw new NoticeException(NoticeErrorCode.NOTICE_DELETE_FAIL);
        }
    }

    // 글 한 건 가져오기
    public Notice getContent(long noticeId) {

        try {
            Notice notice = noticeMapper.findById(noticeId);
            if (notice == null) {
                throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
            }
            return notice;
        } catch (Exception e) {
            e.printStackTrace();    // 개발자를 위해 로그 남기기
            throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }

    // 글 목록 가져오기
    public List<Notice> getList() {
        try {
            return noticeMapper.findAll();
        } catch (Exception e) {
            throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
        }
    }

    // 글 한 건 수정
    public Notice update(Notice notice) {
        // 바로 수정하지 말고, 넘겨받은 정보를 이용하여 먼저 그 대상이 존재하는 지부터 체크
        try {
            Notice found = noticeMapper.findById(notice.getNoticeId());
            if (found == null) {    // 글이 존재하지 않는다면, 수정 실패 처리
                throw new NoticeException(NoticeErrorCode.NOTICE_NOT_FOUND);
            }
            // 위의 예외가 아니라면 수정 진행
            int affeted = noticeMapper.update(notice);
            if (affeted != 1) {
                throw new NoticeException(NoticeErrorCode.NOTICE_UPDATE_FAIL);
            }
            return noticeMapper.findById(notice.getNoticeId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}