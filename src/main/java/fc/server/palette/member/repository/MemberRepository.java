package fc.server.palette.member.repository;

import fc.server.palette.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String email);


}
