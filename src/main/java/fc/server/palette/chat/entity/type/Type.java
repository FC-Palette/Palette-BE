package fc.server.palette.chat.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    SECONDHAND("중고거래"),
    MEETING("모임"),
    PURCHASE("공동구매");

    private final String description;
}
