package com.simter.domain.member.controller;


import com.simter.domain.member.dto.MemberResponseDto.LoginResponseDto;
import com.simter.domain.member.entity.Member;
import com.simter.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/main")
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<LoginResponseDto> getMainInfo(@AuthenticationPrincipal Member currentMember) {
        // 인증된 사용자가 없는 경우 처리
        if (currentMember == null) {
            return ResponseEntity.badRequest().build();
        }

        // 로그인 응답 생성
        LoginResponseDto response = memberService.login(currentMember.getEmail(), currentMember.getPassword());
        return ResponseEntity.ok(response);
    }
}
// 설명 부분 : @AuthenticationPrincipal 부분은 스프링 서큐리티의 userdetail에서 구현이 되어야 가져올수 있음 여기 상황에서
// 가져올 수 있어서 가져왔고, memberservice에서 로그인 응답에 대한 dto 객체를 생성함