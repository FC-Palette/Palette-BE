package fc.server.palette.purchase.repository;

import fc.server.palette.purchase.entity.ParticipantMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseParticipantMemberRepository extends JpaRepository<ParticipantMember, Long> {
    List<ParticipantMember> findAllByMemberId(Long memberId);
}
