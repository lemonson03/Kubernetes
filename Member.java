package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 유저 고유 식별자

    @Column(nullable = false, unique = true)
    private String email;  // 로그인에 사용하는 이메일 (고유값)

    @Column(nullable = false)
    private String password;  // 암호화된 비밀번호 (256자 이내)

    @Column(nullable = false)
    private String nickname;  // 서비스 내에서 사용되는 닉네임 (중복 허용)

    @Column(length = 256)
    private String refreshToken;  // 로그인 시 생성되는 리프레시 토큰 (256자 이내)

    @OneToOne(mappedBy = "receiver", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Letter letter1;  // 현재 수신한 종이비행기

    // 기본 생성자
    public Member() {}

    // Getters and Setters
    // ...
}
