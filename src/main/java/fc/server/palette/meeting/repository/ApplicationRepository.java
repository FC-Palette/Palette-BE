package fc.server.palette.meeting.repository;

import fc.server.palette.meeting.entity.Application;
import fc.server.palette.meeting.entity.Meeting;
import fc.server.palette.meeting.entity.type.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByMeetingAndStatus(Meeting meeting, Status status);

    Application findByMeetingIdAndMemberIdAndStatus(Long meetingId, Long loginMember, Status status);

    List<Application> findByMemberId(Long memberId);

    boolean existsByMeetingIdAndMemberId(Long meetingId, Long id);

    Application findByMeetingIdAndMemberIdAndStatusIn(Long meetingId, Long id, List<Status> waiting);
}
