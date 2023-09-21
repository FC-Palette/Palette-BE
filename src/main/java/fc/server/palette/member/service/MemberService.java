package fc.server.palette.member.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.Exception403;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette.member.dto.request.MemberProfileDto;
import fc.server.palette.member.dto.response.FollowInfoDto;
import fc.server.palette.member.dto.response.MemberMyPageDto;
import fc.server.palette.member.entity.Building;
import fc.server.palette.member.entity.Follow;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.entity.Room;
import fc.server.palette.member.repository.FollowRepository;
import fc.server.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void updateMember(Long loginMemberId, Long memberId, MemberProfileDto memberProfileDto){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new Exception400(memberId.toString(), ExceptionMessage.NO_MEMBER_ID));
        if (member.getId() != loginMemberId) {
            throw new Exception403(ExceptionMessage.ACCESS_DENIED);
        }

        member.updateProfile(memberProfileDto);

    }


    public MemberMyPageDto myPageInfo(Long memberId) {
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findById(memberId).orElseThrow(
                () -> new Exception400(memberId.toString(), ExceptionMessage.NO_MEMBER_ID)));

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            Optional<Building> buildingOptional = Optional.ofNullable(member.getBuilding());
            Optional<Room> roomOptional = Optional.ofNullable(member.getRoom());

            // 팔로워 수와 팔로우 수 조회
            long followedCount = getFollowedCount(memberId);
            long followingCount = getFollowingCount(memberId);


            return MemberMyPageDto.builder()
                    .nickname(member.getNickname() != null ? member.getNickname() : member.getName())
                    .image(member.getImage())
                    .bio(member.getBio())
                    .job(member.getJob() != null ? member.getJob().getValue() : null)
                    .position(member.getPosition() != null ? member.getPosition().getValue() : null)
                    .building(buildingOptional.map(Building::getName).orElse(null))
                    .wing(roomOptional.map(Room::getWing).orElse(null))
                    .roomNumber(roomOptional.map(Room::getRoomNumber).orElse(null))
                    .followedCount(followedCount) // 팔로워 수 설정
                    .followingCount(followingCount) // 팔로우 수 설정
                    .build();
        } else {
            return null;
        }

    }

    // 팔로워 수 조회
    public long getFollowedCount(Long memberId) {
        return followRepository.countByFollowed_Id(memberId);
    }

    // 팔로우 수 조회
    public long getFollowingCount(Long memberId) {
        return followRepository.countByFollowing_Id(memberId);
    }


}







