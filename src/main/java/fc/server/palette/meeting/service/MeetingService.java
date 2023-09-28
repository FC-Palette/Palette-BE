package fc.server.palette.meeting.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette._common.s3.S3DirectoryNames;
import fc.server.palette._common.s3.S3ImageUploader;
import fc.server.palette.meeting.dto.request.ApplicationRequestDto;
import fc.server.palette.meeting.dto.request.MeetingCreateRequestDto;
import fc.server.palette.meeting.dto.request.MeetingUpdateRequestDto;
import fc.server.palette.meeting.dto.response.MeetingDetailResponseDto;
import fc.server.palette.meeting.dto.response.MeetingListResponseDto;
import fc.server.palette.meeting.dto.response.MeetingMemberResponseDto;
import fc.server.palette.meeting.dto.response.WaitingParticipateMemberResponseDto;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public List<MeetingListResponseDto> getMeetingList(Boolean isClose){
        List<Meeting> meetings = meetingRepository.findAll();
        List<MeetingListResponseDto> meetingResponseDtoList = new ArrayList<>();
        if (isClose) {
            for (Meeting meeting : meetings) {
                if (!meeting.isClosing()) {
                    MeetingListResponseDto meetingResponseDto = meetingListResponseDtoBuilder(meeting);
                    meetingResponseDtoList.add(meetingResponseDto);
                }
            }
        }
        else {
            for (Meeting meeting : meetings){
                MeetingListResponseDto meetingResponseDto = meetingListResponseDtoBuilder(meeting);
                meetingResponseDtoList.add(meetingResponseDto);
            }
        }
        return meetingResponseDtoList;
    }

    public List<MeetingListResponseDto> getMeetingFilterList(
            Boolean isClose, String filter, String onOff, String type,
            List<String> job, String position, String sex) {
        List<Meeting> meetings = meetingRepository.findAll();
        List<MeetingListResponseDto> meetingListResponseDtoList = new ArrayList<>();

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
            MeetingListResponseDto meetingListResponseDto = meetingListResponseDtoBuilder(meeting);
            return meetingListResponseDto;
        }).collect(Collectors.toList());
        return meetingListResponseDtoList;
    }

    public void createMeeting(MeetingCreateRequestDto meetingCreateRequestDto, Member member, List<MultipartFile> images) throws IOException {
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

        Meeting meeting = Meeting.builder()
                .member(member)
                .category(Category.fromValue(meetingCreateRequestDto.getCategory()))
                .type(Type.fromValue(meetingCreateRequestDto.getType()))
                .job(Job.fromValue(meetingCreateRequestDto.getJobs()))
                .position(Position.fromValue(meetingCreateRequestDto.getPositions()))
                .sex(Sex.fromValue(meetingCreateRequestDto.getSex()))
                .image(mediaList)
                .title(meetingCreateRequestDto.getTitle())
                .description(meetingCreateRequestDto.getDescription())
                .headCount(meetingCreateRequestDto.getHeadCount())
                .startDate(meetingCreateRequestDto.getStartDate())
                .endDate(meetingCreateRequestDto.getEndDate())
                .onOff(meetingCreateRequestDto.isOnOff())
                .place(meetingCreateRequestDto.getPlace())
                .week(Week.fromValue(meetingCreateRequestDto.getWeek()))
                .days(Day.fromValue(meetingCreateRequestDto.getDays()))
                .time(meetingCreateRequestDto.getTime())
                .progressTime(meetingCreateRequestDto.getProgressTime())
                .acceptType(AcceptType.fromValue(meetingCreateRequestDto.getAcceptType()))
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
        meetingRepository.deleteById(meetingId);
    }

    public void updateMeeting(Long meetingId, MeetingUpdateRequestDto meetingUpdateRequestDto, List<MultipartFile> images) throws IOException {
        Meeting meeting = getMeeting(meetingId);

        List<Media> existingMedia = meeting.getImage();
        existingMedia.stream().forEach(mediaRepository::delete);

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
        meeting.update(meetingUpdateRequestDto, mediaList);

    }

    public MeetingDetailResponseDto getDetailMeeting(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        meeting.setHits(); //조회수 증가
        MeetingMemberResponseDto responseMember = MeetingMemberResponseDto.builder()
                .id(meeting.getMember().getId())
                .nickname(meeting.getMember().getNickname())
                .bio(meeting.getMember().getBio())
                .image(meeting.getMember().getImage())
                .build();
        return MeetingDetailResponseDto.builder()
                .meetingMemberResponseDto(responseMember)
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
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .onOff(meeting.isOnOff())
                .place(meeting.getPlace())
                .week(meeting.getWeek().getDescription())
                .days(meeting.getDays().stream()
                        .map(Day::getDescription)
                        .collect(Collectors.toList()))
                .time(meeting.getTime())
                .progressTime(meeting.getProgressTime())
                .acceptType(meeting.getAcceptType().getDescription())
                .hits(meeting.getHits())
                .likes(meeting.getLikes())
                .createdAt(meeting.getCreatedAt())
                .build();
    }

    public MeetingListResponseDto meetingListResponseDtoBuilder(Meeting meeting){
         return MeetingListResponseDto.builder()
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
                .week(meeting.getWeek().getDescription())
                .days(meeting.getDays().stream()
                        .map(Day::getDescription)
                        .collect(Collectors.toList()))
                .time(meeting.getTime())
                .progressTime(meeting.getProgressTime())
                .isClosing(meeting.isClosing())
                .hits(meeting.getHits())
                .likes(meeting.getLikes())
                .createdAt(meeting.getCreatedAt())
                .build();
    }

    public void likesMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);
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
        if (bookmark != null) {
            bookmarkRepository.delete(bookmark);
        }
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

    public List<MeetingListResponseDto> recommendMeeting(Long loginMemberId, Long meetingId) {
        Meeting baseMeeting = getMeeting(meetingId);
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
                .map(this::meetingListResponseDtoBuilder)
                .collect(Collectors.toList());
    }

    public boolean checkParticipateMeeting(Long meetingId, Member member) {
        Meeting meeting = getMeeting(meetingId);

        return meeting.getPosition().stream()
                .anyMatch(position -> member.getPosition().equals(position)
                && meeting.getSex().equals(member.getSex()));
    }

    public List<MeetingMemberResponseDto> participateMemberList(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Application> applications = applicationRepository.findByMeetingAndStatus(meeting, Status.APPROVAL);

        List<MeetingMemberResponseDto> approvedMembers = applications.stream()
                .map(application -> MeetingMemberResponseDto.builder()
                        .nickname(application.getMember().getNickname())
                        .bio(application.getMember().getBio())
                        .image(application.getMember().getImage())
                        .build())
                .collect(Collectors.toList());
        return approvedMembers;
    }

    public void participateMeeting(Long meetingId, Member member, ApplicationRequestDto applicationRequestDto) {
        Meeting meeting = getMeeting(meetingId);

        Application application = Application.builder()
                .meeting(meeting)
                .member(member)
                .pr(applicationRequestDto.getPr())
                .build();
        applicationRepository.save(application);
    }

    public List<WaitingParticipateMemberResponseDto> waitingParticipateMemberList(Long meetingId) {
        Meeting meeting = getMeeting(meetingId);
        List<Application> applications = applicationRepository.findByMeetingAndStatus(meeting, Status.WAITING);

        List<WaitingParticipateMemberResponseDto> waitingMember = applications.stream()
                .map(application -> WaitingParticipateMemberResponseDto.builder()
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

    public void approveParticipateMember(List<Long> participateIdList){
        for(Long id: participateIdList){
            Application application = applicationRepository.findById(id)
                    .orElseThrow(() -> new Exception400(id.toString(), ExceptionMessage.NO_APPLICATION_ID));
            if ((application.getMeeting().getHeadCount() - application.getMeeting().getRecruitedPersonnel()) < participateIdList.size()) {
                throw new Exception400(application.getMeeting().getTitle(), ExceptionMessage.OVER_CAPACITY);
            }
            application.participateApprove();
            application.getMeeting().setRecruitedPersonnel();
        }
    }

    public Meeting getMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new Exception400(meetingId.toString(), ExceptionMessage.NO_MEMBER_ID));
        return meeting;
    }

}
