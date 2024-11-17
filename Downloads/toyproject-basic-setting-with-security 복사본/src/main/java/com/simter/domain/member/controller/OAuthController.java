package com.simter.domain.member.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.simter.config.JwtTokenProvider;
import com.simter.domain.member.dto.JwtTokenDto;
import com.simter.domain.member.dto.MemberRequestDto.SocialLoginDto;
import com.simter.domain.member.repository.MemberRepository;
import com.simter.domain.member.service.KakaoOAuthService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuthController {
    private final KakaoOAuthService kakaoOAuthService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/v1/login/kakao")
    public ResponseEntity<SocialLoginDto> login(@RequestParam("code") String code)
        throws IOException {
        JsonNode res = kakaoOAuthService.getAccessToken(code);
        String accessToken = res.get("access_token").toString();
        String email = kakaoOAuthService.getEmail(accessToken);

        JwtTokenDto token = jwtTokenProvider.generateSocialToken(email);

        SocialLoginDto response = SocialLoginDto.builder()
            .loginType("kakao")
            .token(token)
            .email(email)
            .isMember(memberRepository.existsByEmail(email))
            .build();
        return ResponseEntity.ok(response);

    }
}
