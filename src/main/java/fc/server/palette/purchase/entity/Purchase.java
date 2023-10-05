package fc.server.palette.purchase.entity;

import fc.server.palette._common.auditing.BaseEntity;
import fc.server.palette.member.entity.Member;
import fc.server.palette.purchase.dto.request.EditOfferDto;
import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.purchase.entity.type.ClosingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@DynamicInsert
public class Purchase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false, length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(nullable = false)
    private String shopUrl;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Integer headCount;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'HEAD_COUNT'")
    @Column(length = 30)
    private ClosingType closingType;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean isClosing;

    @Column(nullable = false, length = 50)
    private String bank;

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 50)
    private String accountOwner;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer hits;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Media> mediaList;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PurchaseParticipant> purchaseParticipants;

    // db에 저장된 기본값이 적용되지 않고 필드에 null로 저장되는데 따른 조치
    public ClosingType getClosingType() {
        return closingType == null ? ClosingType.HEAD_COUNT : closingType;
    }

    public Boolean getIsClosing() {
        return isClosing != null && isClosing;
    }

    public Integer getHits() {
        return hits == null ? 0 : hits;
    }

    public void updateOffer(EditOfferDto editOfferDto) {
        this.shopUrl = editOfferDto.getShopUrl();
        this.startDate = editOfferDto.getStartDate();
        this.endDate = editOfferDto.getEndDate();
        this.headCount = editOfferDto.getHeadCount();
        this.price = editOfferDto.getPrice();
        this.description = editOfferDto.getDescription();
        this.closingType = editOfferDto.getClosingType();
        this.bank = editOfferDto.getBank();
        this.accountNumber = editOfferDto.getAccountNumber();
        this.accountOwner = editOfferDto.getAccountOwner();
    }

    public void closeOffer() {
        this.isClosing = true;
    }

    public void increaseHit() {
        this.hits++;
    }
}
