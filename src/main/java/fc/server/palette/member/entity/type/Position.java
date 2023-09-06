package fc.server.palette.member.entity.type;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Position {
    INTERN("인턴"), EMPLOYEE("사원"),ASSISTANTMANAGER("대리"),
    SENIORMANAGER("과장"), DEPUTYMANAGER("차장"),GENERALMANAGER("부장"),
    BOSS("사장"),MANAGER("매니저"),ETC("기타"), IRRELEVANT("무관");

    private final String value;
}
