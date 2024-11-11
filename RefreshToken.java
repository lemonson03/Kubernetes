package com.example.demo.entity;
import jakarta.persistence.*;
import java.util.Date;
import java.time.LocalDateTime;
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

@Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

@Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
