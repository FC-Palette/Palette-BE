package fc.server.palette.meeting.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeetingMemberDto {
    private String nickname;
    private String bio;
    private String image;
}
