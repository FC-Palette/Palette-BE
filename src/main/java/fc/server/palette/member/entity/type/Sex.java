package fc.server.palette.member.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
    FEMALE("여성"), MALE("남성"), IRRELEVANT("무관");

    private final String value;
}
