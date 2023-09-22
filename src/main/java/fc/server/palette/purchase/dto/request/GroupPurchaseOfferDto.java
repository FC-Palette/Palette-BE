package fc.server.palette.purchase.dto.request;

import fc.server.palette.member.entity.Member;
import fc.server.palette.purchase.entity.Purchase;
import fc.server.palette.purchase.entity.type.Category;
import fc.server.palette.purchase.entity.type.ClosingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GroupPurchaseOfferDto {
    private String title;
    private Category category;
    private Date startDate;
    private Date endDate;
    private Integer headCount;
    private String description;
    private Integer price;
    private String shopUrl;
    //todo 이미지 필드 추가
    private ClosingType closingType;
    private String bank;
    private String accountNumber;
    private String accountOwner;

    public Purchase toEntity(Member member) {
        return Purchase.builder()
                .member(member)
                .title(this.title)
                .category(this.category)
                .shopUrl(this.shopUrl)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .headCount(this.headCount)
                .price(this.price)
                .description(this.description)
                .closingType(this.closingType)
                .bank(this.bank)
                .accountNumber(this.accountNumber)
                .accountOwner(this.accountOwner)
                .build();
    }
}
