package fc.server.palette.purchase.entity.type;

import lombok.Getter;

@Getter
public enum ClosingType {
    DATETIME("일시"),
    HEAD_COUNT("인원");

    private final String closingCriteria;

    ClosingType(String closingCriteria){
        this.closingCriteria=closingCriteria;
    }
}
