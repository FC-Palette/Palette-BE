package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    STUDY("스터디"), PROJECT("프로젝트"), AMITY("친목");

    private final String description;
}
