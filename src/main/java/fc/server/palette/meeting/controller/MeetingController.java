package fc.server.palette.meeting.controller;

import fc.server.palette.meeting.dto.request.ApplicationRequestDto;
import fc.server.palette.meeting.dto.request.MeetingCreateRequestDto;
import fc.server.palette.meeting.dto.request.MeetingUpdateRequestDto;
import fc.server.palette.meeting.dto.response.MeetingDetailResponseDto;
import fc.server.palette.meeting.dto.response.MeetingListResponseDto;
import fc.server.palette.meeting.service.MeetingService;
import fc.server.palette.member.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/develop")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @GetMapping("")
    public ResponseEntity<?> getMeetingList(@RequestParam Boolean isClose){
        List<MeetingListResponseDto> meetingResponseDtoList;
        meetingResponseDtoList = meetingService.getMeetingList(isClose);
        return ResponseEntity.ok(meetingResponseDtoList);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getMeetingFilterList(
            @RequestParam Boolean isClose,
            @RequestParam String filter,
            @RequestParam String onOff,
            @RequestParam String type,
            @RequestParam List<String> job,
            @RequestParam String position,
            @RequestParam String sex
    ){
        List<MeetingListResponseDto> meetingListResponseDtoList = meetingService.getMeetingFilterList(isClose, filter, onOff, type, job, position, sex);
        return ResponseEntity.ok(meetingListResponseDtoList);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart(value = "dto") MeetingCreateRequestDto meetingCreateRequestDto,
            @RequestPart(value = "file", required = false)List<MultipartFile> images
    ){
        MeetingDetailResponseDto meetingDetailResponseDto = meetingService.createMeeting(meetingCreateRequestDto, userDetails.getMember(), images);
        return ResponseEntity.ok(meetingDetailResponseDto);
    }

    @PostMapping("/update/{meetingId}")
    public ResponseEntity<?> updateMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @RequestBody MeetingUpdateRequestDto meetingUpdateRequestDto
    ){
        meetingService.updateMeeting(userDetails.getMember().getId(), meetingId, meetingUpdateRequestDto);
        return ResponseEntity.ok("업데이트 완료");

    }

    @PostMapping("/delete/{meetingId}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Long meetingId){
        meetingService.delete(meetingId);
        return ResponseEntity.ok("삭제완료");
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<?> detailMeeting(@PathVariable Long meetingId){
        return ResponseEntity.ok(meetingService.getDetailMeeting(meetingId));
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
    public ResponseEntity<?> closeMeeting(@PathVariable Long meetingId){
        meetingService.closeMeeting(meetingId);
        return ResponseEntity.ok("모집 마감되었습니다.");
    }

    @PostMapping("/reopen/{meetingId}")
    public ResponseEntity<?> reopenMeeting(@PathVariable Long meetingId){
        meetingService.reopenMeeting(meetingId);
        return ResponseEntity.ok("모집이 시작되었습니다.");
    }

    @GetMapping("/recommend/{meetingId}")
    public ResponseEntity<?> recommendMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        return ResponseEntity.ok(meetingService.recommendMeeting(userDetails.getMember().getId(), meetingId));
    }

    @PostMapping("/participate/check/{meetingId}")
    public ResponseEntity<?> checkParticipate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId
    ){
        if(!meetingService.checkParticipateMeeting(meetingId, userDetails.getMember())){
            return ResponseEntity.ok("가입요건이 맞지 않아요");
        }
        else {
            return ResponseEntity.ok("가입요건이 충족되었습니다.");
        }
    }

    @GetMapping("/{meetingId}/member")
    public ResponseEntity<?> participateMember(@PathVariable Long meetingId){
        return ResponseEntity.ok(meetingService.participateMemberList(meetingId));
    }

    @PostMapping("/participate/{meetingId}")
    public ResponseEntity<?> participateMeeting(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long meetingId,
            @RequestBody ApplicationRequestDto applicationRequestDto
    ){
        meetingService.participateMeeting(meetingId, userDetails.getMember(), applicationRequestDto);
        return ResponseEntity.ok("모임 신청이 완료되었습니다");
    }

}
