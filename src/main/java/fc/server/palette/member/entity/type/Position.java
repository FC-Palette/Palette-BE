package fc.server.palette.member.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Position {
    INTERN("인턴"), EMPLOYEE("사원"),ASSISTANTMANAGER("대리"),
    SENIORMANAGER("과장"), DEPUTYMANAGER("차장"),GENERALMANAGER("부장"),
    BOSS("사장"),MANAGER("매니저"),ETC("기타"), IRRELEVANT("무관");

    private final String value;

    @JsonCreator
    public static List<Position> fromValue(List<String> values) {
        List<Position> positions = new ArrayList<>();
        for (String value : values) {
            for (Position position : Position.values()) {
                if (position.getValue().equals(value)) {
                    positions.add(position);
                    break;
                }
            }
        }
        if (positions.isEmpty()) {
            throw new IllegalArgumentException("Invalid job values: " + values);
        }
        return positions;
    }

    @JsonCreator
    public static Position fromOneValue(String value) {
        for (Position position : Position.values()) {
            if (position.getValue().equals(value)) {
                return position;
            }
        }
        throw new IllegalArgumentException("Invalid position: " + value);
    }
}
