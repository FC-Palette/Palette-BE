package fc.server.palette.member.service;

import fc.server.palette._common.exception.Exception400;
import fc.server.palette._common.exception.Exception403;
import fc.server.palette._common.exception.message.ExceptionMessage;
import fc.server.palette.member.dto.request.MemberProfileDto;
import fc.server.palette.member.entity.Member;
import fc.server.palette.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public void updateMember(Long loginMemberId, Long memberId, MemberProfileDto memberProfileDto){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new Exception400(memberId.toString(), ExceptionMessage.NO_MEMBER_ID));
        if (member.getId() != loginMemberId) {
            throw new Exception403(ExceptionMessage.ACCESS_DENIED);
        }

        member.updateProfile(memberProfileDto);

    }




}







