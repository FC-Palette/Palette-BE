package fc.server.palette.meeting.entity;

import fc.server.palette.meeting.entity.type.*;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.member.entity.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Job job;

    @Column(nullable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Age ageRange;

    @Transient
    private List<Media> image = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(name = "head_count", nullable = false)
    private int headCount;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "on_off", nullable = false)
    private boolean onOff;

    @Column(length = 30)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Week week;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Day day;

    @Column(nullable = false, length = 45)
    private String time;

    @Column(name = "progress_time", nullable = false, length = 10)
    private String progressTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "accept_type", nullable = false)
    private AcceptType acceptType;

    @Column(name = "is_closing", nullable = false)
    private boolean isClosing;
}
