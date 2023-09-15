package fc.server.palette.meeting.entity;

import fc.server.palette._common.auditing.BaseEntity;
import fc.server.palette.meeting.dto.request.MeetingCreateRequestDto;
import fc.server.palette.meeting.entity.type.*;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.member.entity.type.Sex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "meeting")
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Builder.Default
    @ElementCollection(targetClass = Job.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Job> job = new ArrayList<>();

    @Builder.Default
    @ElementCollection(targetClass = Position.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Position> position = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Builder.Default
    @ElementCollection(targetClass = Age.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private List<Age> ageRange = new ArrayList<>();

    @Builder.Default
    @Transient
    private List<Media> image = new ArrayList<>();

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(name = "head_count", nullable = false)
    private int headCount;

    @Column(name = "recruited_personnel")
    private int recruitedPersonnel;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "on_off", nullable = false)
    private boolean onOff;

    @Column(length = 30)
    private String place;

    @Enumerated(EnumType.STRING)
    private Week week;

    @ElementCollection(targetClass = Day.class)
    @Enumerated(EnumType.STRING)
    private List<Day> days;

    @Column(nullable = false, length = 45)
    private String time;

    @Column(name = "progress_time", nullable = false, length = 10)
    private String progressTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "accept_type", nullable = false)
    private AcceptType acceptType;

    @Column(name = "is_closing", nullable = false)
    private boolean isClosing;

    @Builder.Default
    @Column(nullable = false)
    private Integer hits = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer likes = 0;

}
