package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcceptType {
    FIRST_COME("선착순"), APPROVAL("승인");

    private final String description;
}
