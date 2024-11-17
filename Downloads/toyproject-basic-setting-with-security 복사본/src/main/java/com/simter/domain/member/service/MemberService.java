package com.simter.domain.member.service;

import com.simter.config.JwtTokenProvider;
import com.simter.domain.member.converter.MemberConverter;
import com.simter.domain.member.dto.JwtTokenDto;
import com.simter.domain.member.dto.MemberRequestDto.RegisterDto;
import com.simter.domain.member.dto.MemberRequestDto.SocialRegisterDto;
import com.simter.domain.member.dto.MemberResponseDto.EmailValidationResponseDto;
import com.simter.domain.member.dto.MemberResponseDto.LoginResponseDto;
import com.simter.domain.member.entity.Member;
import com.simter.domain.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender mailSender;

    //회원가입
    public void register(RegisterDto registerRequestDto) {
        String email = registerRequestDto.getEmail();
        String password = registerRequestDto.getPassword();
        String nickname = registerRequestDto.getNickname();
        validateRegister(email, password, nickname);
        String encryptedPassword = encoder.encode(password);
        RegisterDto newRegisterDto = RegisterDto.builder()
            .email(email)
            .password(encryptedPassword)
            .nickname(nickname)
            .build();
        Member member = MemberConverter.convertToEntity(newRegisterDto);
        if (!memberRepository.existsByEmail(email)) {
            memberRepository.save(member);
        }
    }

    //소셜 회원가입
    public void register(SocialRegisterDto socialRegisterDto) {
        String email = socialRegisterDto.getEmail();
        String nickname = socialRegisterDto.getNickname();
        JwtTokenDto token = socialRegisterDto.getToken();
        validateNickname(nickname);
        RegisterDto newRegisterDto = RegisterDto.builder()
            .email(email)
            .password("")
            .nickname(nickname)
            .build();
        Member member = MemberConverter.convertToEntity(newRegisterDto);
        if (!memberRepository.existsByEmail(email)) {
            member.setRefreshToken(token.getRefreshToken());
            memberRepository.save(member);
        }
    }

    //로그인
    @Transactional
    public LoginResponseDto login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new Error("로그인 정보가 일치하지 않습니다."));
        if (!encoder.matches(password, member.getPassword())) {
            throw new Error("로그인 정보가 일치하지 않습니다.");
        } else {
            UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication
                = authenticationManager.authenticate(token);
            JwtTokenDto jwtToken = jwtTokenProvider.generateToken(authentication, email);

            return LoginResponseDto.builder()
                .token(jwtToken)
                .build();
        }

    }

    //로그아웃
    public void logout(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new Error("회원을 찾을 수 없습니다."));
        member.setRefreshToken(null);
        memberRepository.save(member);
    }

    //비밀번호 재발송
    public void tempPw(String email) throws MessagingException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new Error("가입되지 않은 메일입니다."));
        Random random = new Random();
        String newPassword = RandomStringUtils.randomAlphanumeric(8 + random.nextInt(9));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[심터] 비밀번호 재설정 메일입니다.");

        String htmlMsg = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px; background-color: #f4f4f9;'>" +
            "  <h2 style='color: #333;'>비밀번호 재설정 안내</h2>" +
            "  <p style='font-size: 16px;'>안녕하세요, " + member.getNickname() + "님!</p>" +
            "  <p style='font-size: 16px;'>임시 비밀번호입니다.</p>" +
            "  <div style='margin: 20px auto; padding: 20px; background-color: #fff; border-radius: 8px; border: 1px solid #ddd; display: inline-block;'>" +
            "    <p style='font-size: 18px; color: #555;'>새 비밀번호: <strong style='font-size: 20px; color: #000;'>" + newPassword + "</strong></p>" +
            "  </div>" +
            "  <p style='font-size: 14px; color: #777;'>로그인 후 비밀번호를 변경하시기 바랍니다.</p>" +
            "  <footer style='margin-top: 30px; font-size: 12px; color: #999;'>문의사항은 <a href='mailto:support@shimter.com'>a64494293@gmail.com</a>으로 연락 주세요.</footer>" +
            "</div>";

        mimeMessageHelper.setText(htmlMsg, true);
        mailSender.send(mimeMessage);

        String encryptedPassword = encoder.encode(newPassword);
        member.setPassword(encryptedPassword);
        memberRepository.save(member);
    }

    //닉네임, 비밀번호, 이메일 유효 검증
    public void validateRegister(String email, String password, String nickname) {
        validateEmail(email);
        validatePassword(password);
        validateNickname(nickname);
    }

    //이메일 중복 조회
    public EmailValidationResponseDto validateDuplicate(String email) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if (findMember.isEmpty()) {
            return EmailValidationResponseDto.builder()
                .isValid(true)
                .build();
        } else {
            return EmailValidationResponseDto.builder()
                .isValid(false)
                .build();
        }
    }

    //이메일 형식 확인
    public void validateEmail(String email) {
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)) {
            throw new Error("이메일 형식이 올바르지 않습니다.");
        }
    }

    //비밀번호 형식 확인
    public void validatePassword(String password) {
        if (!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$", password)) {
            throw new Error("비밀번호 형식이 올바르지 않습니다.");
        }
    }

    //닉네임 형식 확인
    public void validateNickname(String nickname) {
        if (!Pattern.matches("^[가-힣a-zA-Z]{1,10}$", nickname)) {
            throw new Error("닉네임 형식이 올바르지 않습니다.");
        }
    }
}