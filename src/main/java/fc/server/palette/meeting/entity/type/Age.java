package fc.server.palette.meeting.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Age {
    TWENTIES("20대"), THIRTIES("30대"), FORTIES("40대"), FIFTIES("50대"), SIXTIES("60대");

    private final String description;
}
