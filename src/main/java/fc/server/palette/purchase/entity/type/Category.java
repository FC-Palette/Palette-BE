package fc.server.palette.purchase.entity.type;

import lombok.Getter;

@Getter
public enum Category {
    HOUSEHOLD("생활용품"),
    OFFICE_SUPPLIES("사무용품"),
    BEAUTY("뷰티"),
    TICKET("티켓/회원권"),
    FASHION("패션/잡화"),
    HEALTH_AND_FOOD("건강/가공식품"),
    INTERIOR("인테리어"),
    ELECTRONICS("가전/전자제품"),
    BOOKS("도서");

    private final String description;

    Category(String description){
        this.description = description;
    }
}

