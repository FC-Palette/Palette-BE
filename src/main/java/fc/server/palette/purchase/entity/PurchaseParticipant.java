package fc.server.palette.purchase.entity;

import fc.server.palette._common.auditing.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "group_purchase_participant")
public class PurchaseParticipant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    @OneToMany(mappedBy = "purchaseParticipant", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ParticipantMember> participantMemberList;

    public static PurchaseParticipant of(Purchase purchase){
        return PurchaseParticipant.builder()
                .purchase(purchase)
                .build();
    }
}
