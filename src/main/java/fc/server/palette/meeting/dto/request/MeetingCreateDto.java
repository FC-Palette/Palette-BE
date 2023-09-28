package fc.server.palette.meeting.dto.request;

import com.fasterxml.jackson.annotation.*;
import fc.server.palette.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class MeetingCreateDto {
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Member member;
    private Long id;
    private String category;
    private String type;
    private List<String> jobs;
    private List<String> positions;
    private String sex;
    private String title;
    private String description;
    private int headCount;
    private Date startDate;
    private Date endDate;
    private boolean onOff;
    private String place;
    private String week;
    private List<String> days;
    private String time;
    private String progressTime;
    private String acceptType;
}
