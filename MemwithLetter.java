package com.example.demo.entity;

import com.example.demo.entity.Letter;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.example.demo.entity.Member;
import jakarta.persistence.*;
@Entity
@Table (name = "memwithletter")
public class MemwithLetter {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

@ManyToOne
    @JoinColumn(name="letter_id", nullable = false)
    private Letter letter;

@Column(name= "created_at", nullable = false,updatable = false)
    private LocalDateTime createdAt;
}
