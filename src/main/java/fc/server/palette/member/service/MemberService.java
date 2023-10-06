package fc.server.palette.member.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.Exception404;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette._common.s3.S3DirectoryNames;
import fc.server.palette._common.s3.S3ImageUploader;
import fc.server.palette.meeting.dto.response.MeetingListDto;
import fc.server.palette.meeting.entity.Application;
import fc.server.palette.meeting.entity.Meeting;
import fc.server.palette.meeting.repository.ApplicationRepository;
import fc.server.palette.meeting.repository.BookmarkRepository;
import fc.server.palette.meeting.repository.MeetingRepository;
import fc.server.palette.meeting.service.MeetingService;
import fc.server.palette.member.dto.request.MemberProfileDto;
import fc.server.palette.member.dto.response.FollowInfoDto;
import fc.server.palette.member.dto.response.MemberMyPageDto;
import fc.server.palette.member.entity.Building;
import fc.server.palette.member.entity.Follow;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.Room;
import fc.server.palette.member.entity.type.Job;
import fc.server.palette.member.entity.type.Position;
import fc.server.palette.meeting.entity.Media;
import fc.server.palette.meeting.entity.type.Day;
import fc.server.palette.member.repository.FollowRepository;
import fc.server.palette.member.repository.MemberRepository;
import fc.server.palette.purchase.repository.PurchaseBookmarkRepository;
import fc.server.palette.purchase.repository.PurchaseParticipantRepository;
import fc.server.palette.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final MeetingRepository meetingRepository;

    private final MeetingService meetingService;
    private final PurchaseRepository purchaseRepository;
    private final BookmarkRepository bookmarkRepository;
    private final PurchaseBookmarkRepository purchaseBookmarkRepository;

    private final ApplicationRepository applicationRepository;

    private final PurchaseParticipantRepository purchaseParticipantRepository;

    private final S3ImageUploader s3ImageUploader;

    @Transactional
    public void updateMember(Member loginMember, List<MultipartFile> image, MemberProfileDto memberProfileDto) {

        if(image != null) {
            List<String> urlist = s3ImageUploader.save(S3DirectoryNames.MEMBER, image);

            loginMember.changeImageProfile(urlist.get(0));

       }
        if(memberProfileDto != null) {
            loginMember.updateProfile(memberProfileDto);
        }
        memberRepository.save(loginMember);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception400(memberId.toString(), ExceptionMessage.NO_MEMBER_ID));
    }


    public MemberMyPageDto myPageInfo(Long memberId, Member currentMember) {
        Member member = getMember(memberId);



        Building building = member.getBuilding();
        Room room = member.getRoom();


        long followedCount = getFollowedCount(memberId);
        long followingCount = getFollowingCount(memberId);

        boolean isFollowed = followRepository.existsByFollowedAndFollowing(member, currentMember);

        return MemberMyPageDto.builder()
                .sex(member.getSex() != null ? member.getSex().getValue() : null)
                .birthday(member.getBirthday())
                .phoneNumber(member.getPhoneNumber())
                .name(member.getName())
                .nickname(member.getNickname() != null ? member.getNickname() : member.getName())
                .image(member.getImage())
                .bio(member.getBio())
                .job(member.getJob() != null ? member.getJob().getValue() : null)
                .position(member.getPosition() != null ? member.getPosition().getValue() : null)
                .building(building.getName())
                .wing(room.getWing())
                .roomNumber(room.getRoomNumber())
                .followedCount(followedCount)
                .followingCount(followingCount)
                .isFollowed(isFollowed)
                .build();

    }

    public List<MeetingListDto> getMeetingList(Long memberId) {
        List<Application> applications = applicationRepository.findByMemberId(memberId);
        List<MeetingListDto> meetingListDtos = new ArrayList<>();
        for(Application application : applications) {
            Meeting meeting = application.getMeeting();

            MeetingListDto meetingListDto = MeetingListDto.builder()
                    .id(meeting.getId())
                    .category(meeting.getCategory().getDescription())
                    .type(meeting.getType().getDescription())
                    .jobs(meeting.getJob().stream().map(Job::getValue).collect(Collectors.toList()))
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

        meetingListDtos.add(meetingListDto);
        }
        return meetingListDtos;
    }

    public long getFollowedCount(Long memberId) {
        return followRepository.countByFollowedId(memberId);
    }


    public long getFollowingCount(Long memberId) {
        return followRepository.countByFollowingId(memberId);
    }




    public void follow(Long followedId, Long followingId) {
        Optional<Follow> existingFollow = followRepository.findByFollowedIdAndFollowingId(followedId, followingId);

        if (existingFollow.isEmpty()) {
                Follow follow = Follow.builder()
                        .followed(memberRepository.getById(followedId))
                        .following(memberRepository.getReferenceById(followingId))
                        .build();
                followRepository.save(follow);

        }
    }


    public void unfollow(Long followedId, Long followingId) {
        Optional<Follow> existingFollow = followRepository.findByFollowedIdAndFollowingId(followedId, followingId);
        existingFollow.ifPresent(follow -> {
            followRepository.deleteByFollowedIdAndFollowingId(followedId, followingId);
        });
    }


    public List<FollowInfoDto> getFollowed(Long memberId) {
        List<Follow> followerList = followRepository.findByFollowedId(memberId);
        List<FollowInfoDto> followerInfoList = new ArrayList<>();

        for (Follow follow : followerList) {
            Member follower = follow.getFollowing();
            FollowInfoDto dto = followInfo(follower);
            followerInfoList.add(dto);
        }

        return followerInfoList;
    }

    public List<FollowInfoDto> getFollowings(Long memberId) {
        List<Follow> followingList = followRepository.findByFollowingId(memberId);
        List<FollowInfoDto> followingInfoList = new ArrayList<>();

        for (Follow follow : followingList) {
            Member following = follow.getFollowed();
            FollowInfoDto dto = followInfo(following);
            followingInfoList.add(dto);
        }

        return followingInfoList;
    }

    public FollowInfoDto followInfo(Member member) {
        return new FollowInfoDto().builder()
                .memberId(member.getId())
                .image(member.getImage())
                .nickname(member.getNickname())
                .bio(member.getBio())
                .build();
    }

}


