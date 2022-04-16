package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV3", 10_000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(member.getMemberId());

        log.info("findMember={}", findMember);
        log.info("findMember == member {}", (findMember == member));
        log.info("findMember equals member {}", (findMember.equals(member)));

        assertThat(findMember).isEqualTo(member);
    }
}
