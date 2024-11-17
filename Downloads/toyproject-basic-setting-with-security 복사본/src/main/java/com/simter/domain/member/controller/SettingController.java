package com.simter.domain.member.controller;

import com.simter.domain.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.simter.domain.member.dto.MemberRequestDto;
import com.simter.domain.member.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/setting")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    // 닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal Member currentMember,
                                                 @RequestBody MemberRequestDto.RegisterDto request) {
        settingService.updateNickname(currentMember.getId(), request.getNickname());
        return ResponseEntity.ok("Nickname updated successfully");
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<String> updatePassword(@AuthenticationPrincipal Member currentMember,
                                                 @RequestBody MemberRequestDto.LoginRequestDto request) {
        settingService.updatePassword(currentMember.getId(), request.getPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    // 계
    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal Member currentMember) {
        settingService.deleteAccount(currentMember.getId());
        return ResponseEntity.ok("Account deleted successfully");
    }
}