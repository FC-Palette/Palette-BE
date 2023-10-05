package fc.server.palette.secondhand.dto.response;

import fc.server.palette.purchase.entity.type.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductListDto {
    private Long id;
    private String title;
    private Category category;
    private Integer price;
    private String thumbnailUrl;
    private Integer bookmarkCount;
    private Integer hits;
    private Boolean isBookmarked;
    private Boolean isSoldOut;
}
