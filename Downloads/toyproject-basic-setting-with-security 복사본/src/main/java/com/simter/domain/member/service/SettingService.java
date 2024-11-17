package com.simter.domain.member.service;

import com.simter.domain.member.entity.Member;
import com.simter.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 닉네임 변경
    public void updateNickname(Long memberId, String newNickname) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        member.setNickname(passwordEncoder.encode(newNickname)); // 엔티티의 닉네임 필드 변경
        memberRepository.save(member);
    }

    // 비밀번호 변경
    public void updatePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        member.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 암호화 후 저장
        memberRepository.save(member);
    }

    // 계정 탈퇴
    public void deleteAccount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
        memberRepository.delete(member);
    }
}