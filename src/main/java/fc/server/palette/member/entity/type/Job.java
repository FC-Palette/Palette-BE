package fc.server.palette.member.entity.type;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Job {
    BUSINESS("경영 비즈니스"),SERVICE("서비스기획 운영"),DEVELOPMENT("개발"),
    DATA("데이터 AI ML"),MARKETING("마케팅 광고 홍보"),DESIGN("디자인"),
    MEDIA("미디어 커뮤니케이션"),ECOMMERCE("이커머스 리테일"),EDUCATION("교육"),
    FINANCE("금율 컨설팅 VC"), ACCOUNTING("회계 재무"), CUSTOMER("고객 영업"),
    HR("인사 채용 노무"), GAMEDEV("게임기획 개발"), DISTRIBUTION("물류 구매"),
    MEDICAL("의류 제약 바이오"), RESEARCH("연구 개발"),LAW("법률 특허"),
    ENGINEERING("엔지니어링 설계"), PRODUCTION("생산 품질");

    private final String value;
}
