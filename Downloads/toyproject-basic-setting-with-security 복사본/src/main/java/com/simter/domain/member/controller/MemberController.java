package com.simter.domain.member.controller;

import com.simter.config.JwtTokenProvider;
import com.simter.domain.member.dto.JwtTokenDto;
import com.simter.domain.member.dto.MemberRequestDto.LoginRequestDto;
import com.simter.domain.member.dto.MemberRequestDto.PasswordReissueDto;
import com.simter.domain.member.dto.MemberRequestDto.RegisterDto;
import com.simter.domain.member.dto.MemberRequestDto.SocialRegisterDto;
import com.simter.domain.member.dto.MemberResponseDto.EmailValidationResponseDto;
import com.simter.domain.member.dto.MemberResponseDto.LoginResponseDto;
import com.simter.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입 API", description = "이메일, 로그인 타입, 비밀번호, 닉네임을 저장해 회원가입하는 API")
    @PostMapping("/api/v1/register/general")
    public ResponseEntity<Void> register(@RequestBody RegisterDto registerDto) {
        memberService.register(registerDto);
        return ResponseEntity.ok(null);
    }


    @Operation(summary = "회원가입 API", description = "이메일, 로그인 타입, 비밀번호, 닉네임을 저장해 회원가입하는 API")
    @PostMapping("/api/v1/register/social")
    public ResponseEntity<Void> registerSocial(@RequestBody SocialRegisterDto socialRegisterDto) {
        memberService.register(socialRegisterDto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "이메일 중복체크 API", description = "이메일이 이미 가입되어 있는지 조회하는 API")
    @GetMapping("/api/v1/register/general/check")
    public ResponseEntity<EmailValidationResponseDto> checkRegister(@RequestParam String email) {
        EmailValidationResponseDto emailValidationResponseDto = memberService.validateDuplicate(email);
        return ResponseEntity.ok(emailValidationResponseDto);
    }

    @Operation(summary = "일반 로그인 API", description = "이메일, 비밀번호를 입력하여 토큰을 생성하는 API")
    @PostMapping("/api/v1/login/general")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        LoginResponseDto loginResponseDto = memberService.login(email, password);
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "로그아웃 API", description = "리프레시토큰을 파괴하는 API")
    @DeleteMapping("/api/v1/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request).getAccessToken();
        memberService.logout(token);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "토큰 재발급 API", description = "리프레시토큰과 액세스 토큰을 재발급하는 API")
    @GetMapping("/api/v1/reissue")
    public ResponseEntity<JwtTokenDto> reissue(HttpServletRequest request) {
        JwtTokenDto token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getEmail(token.getRefreshToken());
        JwtTokenDto newToken = jwtTokenProvider.reissueToken(email);
        return ResponseEntity.ok(newToken);
    }

    @Operation(summary = "비밀번호 재발송 API", description = "비밃번호를 재생성해서 유저에게 메일 발송하는 API")
    @PatchMapping("/api/v1/login/temp-pw")
    public ResponseEntity<Void> tempPw(@RequestBody PasswordReissueDto passwordReissueDto)
        throws MessagingException {
        memberService.tempPw(passwordReissueDto.getEmail());
        return ResponseEntity.ok(null);
    }
}

