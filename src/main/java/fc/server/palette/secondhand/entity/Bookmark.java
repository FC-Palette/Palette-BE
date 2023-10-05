package fc.server.palette.secondhand.entity;

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
@Entity(name = "SecondhandBookmark")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Secondhand secondhand;

    public static Bookmark of(Secondhand secondhand, Member member){
        return Bookmark.builder()
                .secondhand(secondhand)
                .member(member)
                .build();
    }
}
