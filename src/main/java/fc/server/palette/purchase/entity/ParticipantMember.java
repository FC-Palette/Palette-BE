package fc.server.palette.purchase.entity;

import fc.server.palette.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "group_purchase_participant_member")
public class ParticipantMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseParticipant purchaseParticipant;

    public static ParticipantMember of(Member member, PurchaseParticipant purchaseParticipant){
        return ParticipantMember.builder()
                .member(member)
                .purchaseParticipant(purchaseParticipant)
                .build();
    }
}
