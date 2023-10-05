package fc.server.palette.secondhand.entity;

import fc.server.palette._common.auditing.BaseEntity;
import fc.server.palette.member.entity.Member;
import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.secondhand.dto.request.EditProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@DynamicInsert
public class Secondhand extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, length = 50)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Time transactionStartTime;

    @Column(nullable = false)
    private Time transactionEndTime;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean isSoldOut;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer hits;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean isFree;

    @OneToMany(mappedBy = "secondhand", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Media> mediaList;

    @OneToMany(mappedBy = "secondhand", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarks;


    // db에 저장된 기본값이 적용되지 않고 필드에 null로 저장되는데 따른 조치
    public Boolean getIsSoldOut() {
        return isSoldOut != null && isSoldOut;
    }

    public Integer getHits() {
        return hits == null ? 0 : hits;
    }

    public Boolean getIsFree() {
        return isFree != null && isFree;
    }

    public void closeTransaction() {
        this.isSoldOut = true;
    }

    public void updateProduct(EditProductDto editProductDto) {
        this.price = editProductDto.getPrice();
        this.transactionStartTime = editProductDto.getTransactionStartTime();
        this.transactionEndTime = editProductDto.getTransactionEndTime();
        this.description = editProductDto.getDescription();
    }

    public void increaseHit() {
        this.hits++;
    }
}
