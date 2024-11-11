package com.example.demo.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 편지 고유 식별자

    @Column(nullable = false, length = 256)
    private String content;  // 편지의 내용 (256자 이내)

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 편지 작성 시간

    @Column(nullable = false)
    private String senderNickname;  // 종이비행기 작성자 닉네임 (서비스 닉네임과 다를 수 있음)

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;  // 종이비행기를 발송한 사용자

    @OneToOne
    @JoinColumn(name = "receiver_id", nullable = false, unique = true)
    private Member receiver;  // 종이비행기를 수신한 사용자 (유저당 한 개만 수신 가능)

    // 기본 생성자
    public Letter() {}

    // Getters and Setters
    // ...
}
