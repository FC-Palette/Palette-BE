package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Week {
    EVERY_WEEK("매주"), EVERY_OTHER_WEEK("격주"), MONTHLY("매달");

    private final String description;
}
