package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    APPROVAL("승인"), REFUSE("거절");

    private final String description;
}
