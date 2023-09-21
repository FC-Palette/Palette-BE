package fc.server.palette.member.controller;

import fc.server.palette.member.auth.CustomUserDetails;
import fc.server.palette.member.dto.request.FollowRequestDto;
import fc.server.palette.member.dto.request.MemberProfileDto;
import fc.server.palette.member.dto.response.FollowInfoDto;
import fc.server.palette.member.dto.response.MemberMyPageDto;
import fc.server.palette.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.CacheUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    // 팔로워목록
    @GetMapping("/mypage/followed/{followedId}")
    public ResponseEntity<List<FollowInfoDto>> getFollowed(@PathVariable Long followedId) {
        List<FollowInfoDto> followed = memberService.getFollowed(followedId);
        return ResponseEntity.ok(followed);
    }


    //팔로잉목록
    @GetMapping("/mypage/following/{followingId}")
    public ResponseEntity<List<FollowInfoDto>> getFollowing(@PathVariable Long followingId) {
        List<FollowInfoDto> following = memberService.getFollowings(followingId);
        return ResponseEntity.ok(following);
    }


    //팔로우추가
    @PostMapping("/mypage/follow/{followedId}")
    public ResponseEntity<?> follow(@PathVariable Long followedId, @RequestBody FollowRequestDto Dto ){
        memberService.follow(followedId, Dto.getFollowingId());
        return ResponseEntity.ok("팔로우완료");
    }

    //팔로우삭제
    @DeleteMapping("/mypage/follow/{followedId}/{followingId}")
    public ResponseEntity<?> unfollow(@PathVariable Long followedId, @PathVariable Long followingId) {
        memberService.unfollow(followedId, followingId);
        return ResponseEntity.ok("언팔로우완료");
    }







}
