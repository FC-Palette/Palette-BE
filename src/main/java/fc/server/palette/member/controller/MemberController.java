package fc.server.palette.member.controller;

import fc.server.palette.member.auth.CustomUserDetails;
import fc.server.palette.member.dto.request.MemberProfileDto;
import fc.server.palette.member.dto.response.MemberMyPageDto;
import fc.server.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.CacheUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/mypage/{memberId}")
    public ResponseEntity<?> mypage (@PathVariable Long memberId){
        return ResponseEntity.ok(memberService.myPageInfo(memberId));
    }

    @PostMapping("/mypage/{memberId}")
    public ResponseEntity<?> updateProfile (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long memberId,
            @RequestBody MemberProfileDto memberProfileDto
   ){
       memberService.updateMember(userDetails.getMember().getId(), memberId, memberProfileDto );

        return ResponseEntity.ok("수정완료");
    }



}
