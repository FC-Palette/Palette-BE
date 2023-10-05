package fc.server.palette.secondhand.dto.response;

import fc.server.palette.chat.dto.response.ChatRoomDetailContentDto;
import fc.server.palette.chat.entity.type.ChatRoomType;
import fc.server.palette.purchase.dto.response.MemberDto;
import fc.server.palette.purchase.entity.type.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private MemberDto member;
    private Integer bookmarkCount;
    private List<String> images;
    private String title;
    private Category category;
    private Time transactionStartTime;
    private Time transactionEndTime;
    private String description;
    private Integer price;
    private Integer hits;
    private Boolean isSoldOut;
    private Boolean isFree;
    private LocalDateTime createdAt;
    private List<AnotherProductDto> anotherProductDtos;
    public ChatRoomDetailContentDto toChatRoomInfo() {
        return ChatRoomDetailContentDto.builder()
                .contentId(id)
                .type(ChatRoomType.SECONDHAND)
                .title(title)
                .image(images.get(0))
                .price(price)
                .build();
    }
}
