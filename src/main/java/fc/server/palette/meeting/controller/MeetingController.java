package fc.server.palette.meeting.controller;

import fc.server.palette.chat.entity.type.ChatRoomType;
import fc.server.palette.chat.service.ChatRoomService;
import fc.server.palette.meeting.dto.request.ApplicationDto;
import fc.server.palette.meeting.dto.request.MeetingCreateDto;
import fc.server.palette.meeting.dto.request.MeetingUpdateDto;
import fc.server.palette.meeting.dto.response.MeetingListDto;
import fc.server.palette.meeting.service.MeetingService;
import fc.server.palette.member.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/develop")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final ChatRoomService chatRoomService;

    @GetMapping("")
    public ResponseEntity<?> getMeetingList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Boolean isClose
    ){
        List<MeetingListDto> meetingResponseDtoList;
        meetingResponseDtoList = meetingService.getMeetingList(userDetails.getMember(), isClose);
        return ResponseEntity.ok(meetingResponseDtoList);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getMeetingFilterList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Boolean isClose,
            @RequestParam String filter,
            @RequestParam String onOff,
            @RequestParam String type,
            @RequestParam List<String> job,
            @RequestParam String position,
            @RequestParam String sex
    ){
        List<MeetingListDto> meetingListResponseDtoList = meetingService.getMeetingFilterList(userDetails.getMember(), isClose, filter, onOff, type, job, position, sex);
        return ResponseEntity.ok(meetingListResponseDtoList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "dto") MeetingCreateDto meetingCreateDto,
            @RequestPart(value = "file", required = false)List<MultipartFile> images
    ) {
        meetingService.createMeeting(meetingCreateDto, userDetails.getMember(), images);
        chatRoomService.openGroupChatRoom(meetingCreateDto, userDetails.getMember().getId(), ChatRoomType.MEETING);
        return ResponseEntity.ok("모임을 개설하였습니다.");
    }

    @PostMapping("/update/{meetingId}")
    public ResponseEntity<?> updateMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @RequestPart(value = "dto") MeetingUpdateDto meetingUpdateDto,
            @RequestPart(value = "file", required = false)List<MultipartFile> images
    ) {
        userDetails.validateAuthority(meetingService.getMeeting(meetingId).getMember().getId());
        meetingService.updateMeeting(meetingId, meetingUpdateDto, images);
        return ResponseEntity.ok("업데이트 완료");

    }

    @PostMapping("/delete/{meetingId}")
    public ResponseEntity<?> deleteMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        userDetails.validateAuthority(meetingService.getMeeting(meetingId).getMember().getId());
        meetingService.delete(meetingId);
        return ResponseEntity.ok("삭제완료");
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<?> detailMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        return ResponseEntity.ok(meetingService.getDetailMeeting(userDetails.getMember().getId(), meetingId));
    }

    @PostMapping("/like/{meetingId}")
    public ResponseEntity<?> likesMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        meetingService.likesMeeting(meetingId, userDetails.getMember());
        return ResponseEntity.ok("좋아요를 눌렀습니다.");
    }

    @PostMapping("/like/cancel/{meetingId}")
    public ResponseEntity<?> dislikesMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        meetingService.dislikesMeeting(meetingId, userDetails.getMember());
        return ResponseEntity.ok("좋아요를 취소했습니다.");
    }

    @PostMapping("/close/{meetingId}")
    public ResponseEntity<?> closeMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        userDetails.validateAuthority(meetingService.getMeeting(meetingId).getMember().getId());
        meetingService.closeMeeting(meetingId);
        return ResponseEntity.ok("모집 마감되었습니다.");
    }

    @PostMapping("/reopen/{meetingId}")
    public ResponseEntity<?> reopenMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        userDetails.validateAuthority(meetingService.getMeeting(meetingId).getMember().getId());
        meetingService.reopenMeeting(meetingId);
        return ResponseEntity.ok("모집이 시작되었습니다.");
    }

    @GetMapping("/recommend/{meetingId}")
    public ResponseEntity<?> recommendMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        return ResponseEntity.ok(meetingService.recommendMeeting(userDetails.getMember(), meetingId));
    }

    @PostMapping("/participate/check/{meetingId}")
    public ResponseEntity<?> checkParticipate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        if (!meetingService.checkParticipateMeeting(meetingId, userDetails.getMember())) {
            return ResponseEntity.ok("가입요건이 맞지 않아요");
        }
        return ResponseEntity.ok("가입요건이 충족되었습니다.");
    }

    @GetMapping("/{meetingId}/member")
    public ResponseEntity<?> participateMember(@PathVariable Long meetingId){
        return ResponseEntity.ok(meetingService.participateMemberList(meetingId));
    }

    @PostMapping("/participate/{meetingId}")
    public ResponseEntity<?> participateMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @RequestBody ApplicationDto applicationDto
    ){
        meetingService.participateMeeting(meetingId, userDetails.getMember(), applicationDto);
        return ResponseEntity.ok("모임 신청이 완료되었습니다");
    }

    @PostMapping("/participate/firstCome/{meetingId}")
    public ResponseEntity<?> participateMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        meetingService.participateFirstComeMeeting(meetingId, userDetails.getMember());
        chatRoomService.participantGroupChatRoom(userDetails.getMember().getId(), meetingId, ChatRoomType.MEETING);
        return ResponseEntity.ok("모임 신청이 완료되었습니다");
    }

    @GetMapping("/{meetingId}/participate/member")
    public ResponseEntity<?> waitingParticipateMemberList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        userDetails.validateAuthority(meetingService.getMeeting(meetingId).getMember().getId());
        return ResponseEntity.ok(meetingService.waitingParticipateMemberList(meetingId));
    }

    @PostMapping("/participate/refuse")
    public ResponseEntity<?> refusedParticipateMember(
            @RequestBody List<Long> participateIdList
    ){
        meetingService.refusedParticipateMember(participateIdList);
        return ResponseEntity.ok("거절하였습니다.");
    }

    @PostMapping("/participate/confirm")
    public ResponseEntity<?> confirmParticipateMember(
            @RequestBody List<Long> participateIdList
    ){
        Map<Long, Long> results = meetingService.approveParticipateMember(participateIdList);
        chatRoomService.participantGroupChatRoom(results, ChatRoomType.MEETING);
        return ResponseEntity.ok("승인하였습니다.");
    }

}
