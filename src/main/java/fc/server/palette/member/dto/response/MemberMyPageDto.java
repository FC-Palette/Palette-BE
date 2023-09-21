package fc.server.palette.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberMyPageDto {

    private String nickname;

    private String image;

    private String bio;

    private String job;

    private String position;

    private String building;

    private String wing;

    private String roomNumber;

    private long followedCount;

    private long followingCount;


}
