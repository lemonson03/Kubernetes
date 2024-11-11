package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ChecklistItem {

@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

@ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

@Column(nullable = false, length = 256)
    private String content;

@Column(nullable = false)
    private boolean checked;

public ChecklistItem(){}

}
