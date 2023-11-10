package fc.server.palette.purchase.dto.response;

import fc.server.palette.purchase.entity.type.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class OfferListDto {
    private Long id;
    private String title;
    private Category category;
    private Integer price;
    private String thumbnailUrl;
    private Integer bookmarkCount;
    private Integer hits;
    private Boolean isBookmarked;
    private Boolean isClosing;
    private LocalDate endDate;
    private LocalDateTime created_at;
}
