package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CAREER("커리어"), EXERCISE("근력"), ASSET("자산"), SELF_DEVELOPMENT("자기개발");

    private final String description;
}
