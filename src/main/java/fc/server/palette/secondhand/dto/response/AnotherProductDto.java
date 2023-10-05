package fc.server.palette.secondhand.dto.response;

import fc.server.palette.secondhand.entity.Secondhand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AnotherProductDto {
    private Long id;
    private String title;
    private Integer price;
    private String thumbnailUrl;

    public static AnotherProductDto of(Secondhand secondhand, String thumbnailUrl){
        return AnotherProductDto.builder()
                .id(secondhand.getId())
                .title(secondhand.getTitle())
                .price(secondhand.getPrice())
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
