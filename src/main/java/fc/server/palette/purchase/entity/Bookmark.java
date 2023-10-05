package fc.server.palette.purchase.entity;

import fc.server.palette._common.auditing.BaseEntity;
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
@Entity(name = "PurchaseBookmark")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    public static Bookmark of(Purchase purchase, Member member){
        return Bookmark.builder()
                .purchase(purchase)
                .member(member)
                .build();
    }
}
