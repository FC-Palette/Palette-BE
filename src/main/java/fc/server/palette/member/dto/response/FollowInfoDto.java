package fc.server.palette.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowInfoDto {

    private Long memberId;
    private String image;
    private String nickname;
    private String bio;


}
