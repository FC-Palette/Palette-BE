package fc.server.palette.meeting.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette._common.s3.S3DirectoryNames;
import fc.server.palette._common.s3.S3ImageUploader;
import fc.server.palette.meeting.dto.request.ApplicationDto;
import fc.server.palette.meeting.dto.request.MeetingCreateDto;
import fc.server.palette.meeting.dto.request.MeetingUpdateDto;
import fc.server.palette.meeting.dto.response.MeetingDetailDto;
import fc.server.palette.meeting.dto.response.MeetingListDto;
import fc.server.palette.meeting.dto.response.MeetingMemberDto;
import fc.server.palette.meeting.dto.response.WaitingParticipateMemberDto;
import fc.server.palette.meeting.entity.Application;
import fc.server.palette.meeting.entity.Bookmark;
import fc.server.palette.meeting.entity.Media;
import fc.server.palette.meeting.entity.Meeting;
import fc.server.palette.meeting.entity.type.*;
import fc.server.palette.meeting.repository.ApplicationRepository;
import fc.server.palette.meeting.repository.BookmarkRepository;
import fc.server.palette.meeting.repository.MediaRepository;
import fc.server.palette.meeting.repository.MeetingRepository;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.member.entity.type.Sex;
import fc.server.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MediaRepository mediaRepository;
    private final ApplicationRepository applicationRepository;
    private final S3ImageUploader s3ImageUploader;

    public List<MeetingListDto> getMeetingList(Member member, Boolean isClose){
        List<Meeting> meetings = meetingRepository.findAll();
        List<MeetingListDto> meetingResponseDtoList = new ArrayList<>();
        if (isClose) {
            for (Meeting meeting : meetings) {
                if (!meeting.isClosing()) {
                    MeetingListDto meetingResponseDto = meetingListResponseDtoBuilder(meeting, member);
                    meetingResponseDtoList.add(meetingResponseDto);
                }
            }
        }
        else {
            for (Meeting meeting : meetings){
                MeetingListDto meetingResponseDto = meetingListResponseDtoBuilder(meeting, member);
                meetingResponseDtoList.add(meetingResponseDto);
            }
        }
        return meetingResponseDtoList;
    }

    public List<MeetingListDto> getMeetingFilterList(
            Member member, Boolean isClose,
            String filter, String onOff, String type,
            List<String> job, String position, String sex) {
        List<Meeting> meetings = meetingRepository.findAll();
        List<MeetingListDto> meetingListResponseDtoList = new ArrayList<>();

        if (isClose) {
            meetings = meetings.stream().filter(meeting -> !meeting.isClosing()).collect(Collectors.toList());
        }

        if (!onOff.isEmpty()) {
            boolean isOnline = "온라인".equalsIgnoreCase(onOff);
            meetings = meetings.stream()
                    .filter(meeting -> meeting.isOnOff() != isOnline)
                    .collect(Collectors.toList());
        }

        meetings = meetings.stream()
                .filter(meeting -> (type.isEmpty() || type.equals(meeting.getType().getDescription())) &&
                        (position.isEmpty() || meeting.getPosition().stream()
                                .map(Position::getValue).anyMatch(pos -> position.equals(pos))) &&
                        (sex.isEmpty() || sex.equals(meeting.getSex().getValue())) &&
                        (job.isEmpty() || job.stream().anyMatch(job1 -> meeting.getJob().stream()
                                .map(Job::getValue).anyMatch(job1::equals))))
                        .collect(Collectors.toList());

        switch (filter){
            case "최신순":
                meetings.sort(Comparator.comparing(Meeting::getCreatedAt).reversed());
                break;
            case "찜 많은 순":
                meetings.sort(Comparator.comparing(Meeting::getLikes).reversed());
                break;
            case "조회순":
                meetings.sort(Comparator.comparing(Meeting::getHits).reversed());
                break;
            case "모임시작일 순":
                meetings.sort(Comparator.comparing(Meeting::getStartDate));
                break;
            default:
                meetings.sort(Comparator.comparing(Meeting::getCreatedAt).reversed());
        }

        meetingListResponseDtoList = meetings.stream().map(meeting -> {
            MeetingListDto meetingListDto = meetingListResponseDtoBuilder(meeting, member);
            return meetingListDto;
        }).collect(Collectors.toList());
        return meetingListResponseDtoList;
    }

    public void createMeeting(MeetingCreateDto meetingCreateDto, Member member, List<MultipartFile> images) {
        List<Media> mediaList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        boolean isImageEmpty = images.stream().anyMatch(MultipartFile::isEmpty);

        if (!isImageEmpty) {
            urlList = s3ImageUploader.save(S3DirectoryNames.MEETING, images);
            for(String imageUrl : urlList){
                Media media = Media.builder()
                        .url(imageUrl)
                        .build();
                mediaList.add(media);
            }
        }

        Week week = meetingCreateDto.getWeek() != null && !meetingCreateDto.getWeek().isEmpty()
                ? Week.fromValue(meetingCreateDto.getWeek())
                : null;

        List<Day> days = meetingCreateDto.getDays() != null && !meetingCreateDto.getDays().isEmpty()
                ? Day.fromValue(meetingCreateDto.getDays())
                : Collections.emptyList();

        String progressTime = meetingCreateDto.getProgressTime() != null && !meetingCreateDto.getProgressTime().isEmpty()
                ? meetingCreateDto.getProgressTime()
                : null;

        String time = meetingCreateDto.getTime() != null && !meetingCreateDto.getTime().isEmpty()
                ? meetingCreateDto.getTime()
                : null;

        Meeting meeting = Meeting.builder()
                .member(member)
                .category(Category.fromValue(meetingCreateDto.getCategory()))
                .type(Type.fromValue(meetingCreateDto.getType()))
                .job(Job.fromValue(meetingCreateDto.getJobs()))
                .position(Position.fromValue(meetingCreateDto.getPositions()))
                .sex(Sex.fromValue(meetingCreateDto.getSex()))
                .image(mediaList)
                .title(meetingCreateDto.getTitle())
                .description(meetingCreateDto.getDescription())
                .headCount(meetingCreateDto.getHeadCount())
                .startDate(meetingCreateDto.getStartDate())
                .endDate(meetingCreateDto.getEndDate())
                .onOff(meetingCreateDto.isOnOff())
                .place(meetingCreateDto.getPlace())
                .week(week)
                .days(days)
                .time(time)
                .progressTime(progressTime)
                .acceptType(AcceptType.fromValue(meetingCreateDto.getAcceptType()))
                .build();

        Meeting saveMeeting = meetingRepository.save(meeting);

        for(String url : urlList){
            Media media = Media.builder()
                    .url(url)
                    .meeting(saveMeeting)
                    .build();
            mediaRepository.save(media);
        }

    }

    public void delete(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Media> existingMedia = meeting.getImage();
        List<String> existingImageUrl = existingMedia.stream()
                .map(Media::getUrl)
                .collect(Collectors.toList());
        s3ImageUploader.remove(existingImageUrl);
        meetingRepository.deleteById(meetingId);
    }

    public void updateMeeting(Long meetingId, MeetingUpdateDto meetingUpdateDto, List<MultipartFile> images) {
        Meeting meeting = getMeeting(meetingId);

        List<Media> existingMedia = meeting.getImage();
        if(!existingMedia.isEmpty()){
            existingMedia.forEach(media -> {
                mediaRepository.delete(media);
            });
            List<String> existingImageUrl = existingMedia.stream()
                    .map(Media::getUrl)
                    .collect(Collectors.toList());
            s3ImageUploader.remove(existingImageUrl);
        }

        List<Media> mediaList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        boolean isImageEmpty = images.stream().anyMatch(MultipartFile::isEmpty);

        if (!isImageEmpty) {
            urlList = s3ImageUploader.save(S3DirectoryNames.MEETING, images);
            for(String imageUrl : urlList){
                Media media = Media.builder()
                        .url(imageUrl)
                        .meeting(meeting)
                        .build();
                mediaList.add(media);
                mediaRepository.save(media);
            }
        }

        String week = meetingUpdateDto.getWeek() != null && !meetingUpdateDto.getWeek().isEmpty()
                ? meetingUpdateDto.getWeek()
                : null;

        List<String> days = meetingUpdateDto.getDays() != null && !meetingUpdateDto.getDays().isEmpty()
                ? meetingUpdateDto.getDays()
                : Collections.emptyList();

        String progressTime = meetingUpdateDto.getProgressTime() != null && !meetingUpdateDto.getProgressTime().isEmpty()
                ? meetingUpdateDto.getProgressTime()
                : null;

        String time = meetingUpdateDto.getTime() != null && !meetingUpdateDto.getTime().isEmpty()
                ? meetingUpdateDto.getTime()
                : null;
        meetingUpdateDto.setUpdateValue(week, days, progressTime, time);
        meeting.update(meetingUpdateDto, mediaList);

    }

    public MeetingDetailDto getDetailMeeting(Long loginMember, Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if (!meeting.getMember().getId().equals(loginMember)) {
            meeting.setHits(); //조회수 증가
        }
        String msg = "참여하고 있지않은 모임입니다.";
        boolean likemsg = false;
        Bookmark bookmark = bookmarkRepository.findByMemberIdAndMeeting(loginMember, meeting);
        if (bookmark != null){
            likemsg = true;
        }
        Application application = applicationRepository.findByMeetingIdAndMemberIdAndStatus(meetingId, loginMember, Status.APPROVAL);
        if (application != null) {
            msg = "이미 참여하고있는 모임입니다.";
        }
        String weekDescription = meeting.getWeek() != null ? meeting.getWeek().getDescription() : null;
        List<String> days = meeting.getDays().isEmpty() ? Collections.emptyList() :
                meeting.getDays().stream()
                        .map(Day::getDescription)
                        .collect(Collectors.toList());

        MeetingMemberDto responseMember = MeetingMemberDto.builder()
                .id(meeting.getMember().getId())
                .nickname(meeting.getMember().getNickname())
                .bio(meeting.getMember().getBio())
                .image(meeting.getMember().getImage())
                .build();
        return MeetingDetailDto.builder()
                .meetingMemberDto(responseMember)
                .id(meeting.getId())
                .category(meeting.getCategory().getDescription())
                .type(meeting.getType().getDescription())
                .jobs(meeting.getJob().stream()
                        .map(Job::getValue)
                        .collect(Collectors.toList()))
                .positions(meeting.getPosition().stream()
                        .map(Position::getValue)
                        .collect(Collectors.toList()))
                .sex(meeting.getSex().getValue())
                .image(meeting.getImage().stream()
                        .map(Media::getUrl)
                        .collect(Collectors.toList()))
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .headCount(meeting.getHeadCount())
                .recruitedPersonnel(meeting.getRecruitedPersonnel())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .onOff(meeting.isOnOff())
                .place(meeting.getPlace())
                .week(weekDescription)
                .days(days)
                .time(meeting.getTime())
                .progressTime(meeting.getProgressTime())
                .acceptType(meeting.getAcceptType().getDescription())
                .hits(meeting.getHits())
                .likes(meeting.getLikes())
                .isClosing(meeting.isClosing())
                .createdAt(meeting.getCreatedAt())
                .msg(msg)
                .likemsg(likemsg)
                .build();
    }

    public MeetingListDto meetingListResponseDtoBuilder(Meeting meeting, Member member){
        boolean likemsg = false;
        Bookmark bookmark = bookmarkRepository.findByMemberIdAndMeeting(member.getId(), meeting);
        if (bookmark != null){
            likemsg = true;
        }
        String weekDescription = meeting.getWeek() != null ? meeting.getWeek().getDescription() : null;
        List<String> days = meeting.getDays().isEmpty() ? Collections.emptyList() :
                meeting.getDays().stream()
                        .map(Day::getDescription)
                        .collect(Collectors.toList());
        return MeetingListDto.builder()
                .id(meeting.getId())
                .category(meeting.getCategory().getDescription())
                .type(meeting.getType().getDescription())
                .jobs(meeting.getJob().stream()
                        .map(Job::getValue)
                        .collect(Collectors.toList()))
                .positions(meeting.getPosition().stream().
                        map(Position::getValue)
                        .collect(Collectors.toList()))
                .sex(meeting.getSex().getValue())
                .image(meeting.getImage().stream()
                        .map(Media::getUrl)
                        .collect(Collectors.toList()))
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .headCount(meeting.getHeadCount())
                .recruitedPersonnel(meeting.getRecruitedPersonnel())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .onOff(meeting.isOnOff())
                .place(meeting.getPlace())
                .week(weekDescription)
                .days(days)
                .time(meeting.getTime())
                .progressTime(meeting.getProgressTime())
                .isClosing(meeting.isClosing())
                .hits(meeting.getHits())
                .likes(meeting.getLikes())
                .likemsg(likemsg)
                .createdAt(meeting.getCreatedAt())
                .build();
    }

    public void likesMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);
        Bookmark existingBookmark = bookmarkRepository.findByMemberAndMeeting(member, meeting);
        if (existingBookmark != null) {
            throw new Exception400(member.getNickname(), ExceptionMessage.NO_DUPLICATED_LIKE);
        }
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .meeting(meeting)
                .build();
        bookmarkRepository.save(bookmark);
        meeting.likes();
    }

    public void dislikesMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);
        Bookmark bookmark = bookmarkRepository.findByMemberAndMeeting(member, meeting);
        if (bookmark == null) {
            throw new Exception400(member.getNickname(), ExceptionMessage.NO_DISLIKE);
        }
        bookmarkRepository.delete(bookmark);
        meeting.dislike();
    }

    public void closeMeeting(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting.isClosing()) {
            throw new Exception400(meetingId.toString(), ExceptionMessage.AlREADY_CLOSING);
        }
        meeting.closed();
    }

    public void reopenMeeting(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        meeting.reopen();
    }

    public List<MeetingListDto> recommendMeeting(Member member, Long meetingId) {
        Meeting baseMeeting = getMeeting(meetingId);
        Long loginMemberId = member.getId();
        List<Meeting> recommededMeeting = new ArrayList<>();

        if (recommededMeeting.size() < 2) {
            List<Meeting> sameOnOffMeeting = meetingRepository.findByOnOffAndIsClosingFalse(baseMeeting.isOnOff());
            sameOnOffMeeting.remove(baseMeeting);
            sameOnOffMeeting.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(sameOnOffMeeting.subList(0, Math.min(2, sameOnOffMeeting.size())));
        }

        if (recommededMeeting.size() < 2) {
            List<Meeting> sameTypeMeeting = meetingRepository.findByTypeAndIsClosingFalse(baseMeeting.getType());
            sameTypeMeeting.remove(baseMeeting);
            sameTypeMeeting.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(sameTypeMeeting.subList(0, Math.min(2 - recommededMeeting.size(), sameTypeMeeting.size())));
        }

        if (recommededMeeting.size() < 2) {
            List<Job> jobs = baseMeeting.getJob();
            List<Meeting> sameJobsMeeting = meetingRepository.findByJobInAndIsClosingFalse(jobs);
            sameJobsMeeting.remove(baseMeeting);
            sameJobsMeeting.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(sameJobsMeeting.subList(0, Math.min(2 - recommededMeeting.size(), sameJobsMeeting.size())));
        }

        if (recommededMeeting.size() < 2) {
            List<Position> positions = baseMeeting.getPosition();
            List<Meeting> samePositionsMeetings = meetingRepository.findByPositionInAndIsClosingFalse(positions);
            samePositionsMeetings.remove(baseMeeting);
            samePositionsMeetings.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(samePositionsMeetings.subList(0, Math.min(2 - recommededMeeting.size(), samePositionsMeetings.size())));
        }

        if (recommededMeeting.size() < 2) {
            Sex sex = baseMeeting.getSex();
            List<Meeting> sameSexMeetings = meetingRepository.findBySexAndIsClosingFalse(sex);
            sameSexMeetings.remove(baseMeeting);
            sameSexMeetings.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(sameSexMeetings.subList(0, Math.min(2 - recommededMeeting.size(), sameSexMeetings.size())));
        }

        if (recommededMeeting.size() < 2) {
            List<Meeting> mostLikedMeetings = meetingRepository.findTop2ByIsClosingFalseOrderByLikesDesc();
            mostLikedMeetings.remove(baseMeeting);
            mostLikedMeetings.removeIf(meeting -> meeting.getMember().getId().equals(loginMemberId));
            recommededMeeting.addAll(mostLikedMeetings);
        }

        return recommededMeeting.stream()
                .map(meeting -> meetingListResponseDtoBuilder(meeting, member))
                .collect(Collectors.toList());
    }

    public boolean checkParticipateMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);

        return meeting.getPosition().stream()
                .anyMatch(position -> member.getPosition().equals(position)
                && meeting.getSex().equals(member.getSex()));
    }

    public List<MeetingMemberDto> participateMemberList(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Application> applications = applicationRepository.findByMeetingAndStatus(meeting, Status.APPROVAL);

        List<MeetingMemberDto> approvedMembers = applications.stream()
                .map(application -> MeetingMemberDto.builder()
                        .id(application.getMember().getId())
                        .nickname(application.getMember().getNickname())
                        .bio(application.getMember().getBio())
                        .image(application.getMember().getImage())
                        .build())
                .collect(Collectors.toList());
        return approvedMembers;
    }

    public void participateMeeting(Long meetingId, Member member, ApplicationDto applicationDto) {
        if (applicationDto.getPr() != null && applicationDto.getPr().trim().isEmpty()) {
            throw new Exception400(member.getNickname(), ExceptionMessage.NO_PR);
        }

        Meeting meeting = getMeeting(meetingId);

        Application existingApplication = applicationRepository.findByMeetingIdAndMemberIdAndStatusIn(
                meetingId,
                member.getId(),
                List.of(Status.WAITING, Status.APPROVAL)
        );

        if(meeting.getMember().getId().equals(member.getId())){
            throw new Exception400(member.getNickname(), ExceptionMessage.PARTICIPATE_YOURSELF_DENIED);
        }

        if (existingApplication != null) {
            throw new Exception400(member.getNickname(), ExceptionMessage.ALREADY_APPLIED);
        }

        Application application = Application.builder()
                .meeting(meeting)
                .member(member)
                .pr(applicationDto.getPr())
                .build();
        applicationRepository.save(application);
    }

    public List<WaitingParticipateMemberDto> waitingParticipateMemberList(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Application> applications = applicationRepository.findByMeetingAndStatus(meeting, Status.WAITING);

        List<WaitingParticipateMemberDto> waitingMember = applications.stream()
                .map(application -> WaitingParticipateMemberDto.builder()
                        .id(application.getId())
                        .nickname(application.getMember().getNickname())
                        .bio(application.getMember().getBio())
                        .pr(application.getPr())
                        .image(application.getMember().getImage())
                        .build())
                .collect(Collectors.toList());
        return waitingMember;
    }

    public void refusedParticipateMember(List<Long> participateIdList) {
        for(Long id: participateIdList){
            Application application = applicationRepository.findById(id)
                    .orElseThrow(() -> new Exception400(id.toString(), ExceptionMessage.NO_APPLICATION_ID));
            application.participateRefused();
        }
    }

    public Map<Long, Long> approveParticipateMember(List<Long> participateIdList){
        Map<Long, Long> results = new HashMap<>();
        for(Long id: participateIdList){
            Application application = applicationRepository.findById(id)
                    .orElseThrow(() -> new Exception400(id.toString(), ExceptionMessage.NO_APPLICATION_ID));
            if ((application.getMeeting().getHeadCount() - application.getMeeting().getRecruitedPersonnel()) < participateIdList.size()) {
                throw new Exception400(application.getMeeting().getTitle(), ExceptionMessage.OVER_CAPACITY);
            }
            application.participateApprove();
            application.getMeeting().setRecruitedPersonnel();
            results.put(application.getMeeting().getId(), id);
        }
        return results;
    }

    public Meeting getMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new Exception400(meetingId.toString(), ExceptionMessage.NO_MEMBER_ID));
        return meeting;
    }

    public void participateFirstComeMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);
        Application existingApplication = applicationRepository.findByMeetingIdAndMemberIdAndStatusIn(
                meetingId,
                member.getId(),
                List.of(Status.WAITING, Status.APPROVAL)
        );

        if(meeting.getMember().getId().equals(member.getId())){
            throw new Exception400(member.getNickname(), ExceptionMessage.PARTICIPATE_YOURSELF_DENIED);
        }

        if (existingApplication != null) {
            throw new Exception400(member.getNickname(), ExceptionMessage.ALREADY_APPLIED);
        }

        if ((meeting.getHeadCount() - meeting.getRecruitedPersonnel()) <= 0) {
            throw new Exception400(meeting.getTitle(), ExceptionMessage.OVER_CAPACITY);
        }
        Application application = Application.builder()
                .meeting(meeting)
                .member(member)
                .status(Status.APPROVAL)
                .build();
        applicationRepository.save(application);
        application.getMeeting().setRecruitedPersonnel();
    }
}
