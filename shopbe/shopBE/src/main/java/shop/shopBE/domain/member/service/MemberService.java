package shop.shopBE.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.shopBE.domain.member.entity.Member;
import shop.shopBE.domain.member.exception.MemberExceptionCode;
import shop.shopBE.domain.member.repository.MemberRepository;
import shop.shopBE.domain.member.request.MemberUpdateInfo;
import shop.shopBE.domain.member.response.MemberInformation;
import shop.shopBE.global.exception.custom.CustomException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void updateMember(Long memberId,MemberUpdateInfo memberUpdateInfo) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionCode.MEMBER_NOT_FOUND)); // 변경 감지에 의한 업데이트

        findMember.updateMember(memberUpdateInfo);
    }

    public MemberInformation findMemberInfoById(Long memberId) {
        return memberRepository.findMemberInformById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionCode.MEMBER_NOT_FOUND));
    }

}
