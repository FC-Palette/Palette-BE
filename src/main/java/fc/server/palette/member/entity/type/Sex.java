package fc.server.palette.member.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {

    FEMALE("여성"), MALE("남성"), IRRELEVANT("무관");

    private final String value;

    @JsonCreator
    public static Sex fromValue(String value) {
        for (Sex sex : Sex.values()) {
            if (sex.getValue().equals(value)) {
                return sex;
            }
        }
        throw new IllegalArgumentException("Invalid sex: " + value);
    }
}
