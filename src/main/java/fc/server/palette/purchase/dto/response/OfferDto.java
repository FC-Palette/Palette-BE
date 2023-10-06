package fc.server.palette.purchase.dto.response;

import fc.server.palette.chat.dto.response.ChatRoomDetailContentDto;
import fc.server.palette.chat.entity.type.ChatRoomType;
import fc.server.palette.purchase.entity.type.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OfferDto {
    private Long id;
    private MemberDto member;
    private String title;
    private Category category;
    private String description;
    private String shopUrl;
    private Integer headCount;
    private Integer bookmarkCount;
    private Integer currentParticipantCount;
    private List<String> image;
    private Date startDate;
    private Date endDate;
    private Integer price;
    private Boolean isClosing;
    private Integer hits;
    private LocalDateTime created_at;
    private Boolean isParticipating;
    private Boolean isBookmarked;


    public ChatRoomDetailContentDto toChatRoomInfo() {
        return ChatRoomDetailContentDto.builder()
                .contentId(id)
                .title(title)
                .image(image.get(0))
                .price(price)
                .type(ChatRoomType.PURCHASE)
                .build();
    }
}
