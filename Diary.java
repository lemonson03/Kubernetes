package com.example.demo.entity;
import jakarta.persistence.*;

import java.util.List;
import java.time.LocalDate;

@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    @Column(nullable = false, unique = true)
    private LocalDate diaryDate;

    @Column (length = 256)
    private String mood;

    @Column (length = 256)
    private String oneLineDictionary;


}
