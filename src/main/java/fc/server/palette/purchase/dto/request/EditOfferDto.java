package fc.server.palette.purchase.dto.request;

import fc.server.palette.purchase.entity.type.ClosingType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EditOfferDto {
    private String shopUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer headCount;
    private Integer price;
    private String description;
    private ClosingType closingType;
    private String bank;
    private String accountNumber;
    private String accountOwner;
}
