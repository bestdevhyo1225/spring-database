package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection conn = dataSource.getConnection();

        try {
            conn.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(conn, fromId, toId, money); // 비즈니스 로직 수행
            conn.commit(); // 커밋
        } catch (Exception e) {
            conn.rollback(); // 롤백
            throw new IllegalStateException(e);
        } finally {
            release(conn); // 커넥션 풀을 사용하는 경우, 커넥션 반납
        }
    }

    private void bizLogic(Connection conn, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(conn, fromId);
        Member toMember = memberRepository.findById(conn, toId);

        memberRepository.update(conn, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(conn, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("계좌이체 중 예외 발생!");
        }
    }

    private void release(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // 커넥션 풀을 고려해서 자동 커밋으로 다시 되돌린다.
                conn.close();
            } catch (Exception e) {
                log.error("error", e);
            }
        }
    }
}
