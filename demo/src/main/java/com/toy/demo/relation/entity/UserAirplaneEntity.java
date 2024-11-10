package com.toy.demo.relation.entity;

import com.toy.demo.airplane.entity.AirplaneEntity;
import com.toy.demo.user.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_airplane")
public class UserAirplaneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "airplane_id", nullable = false)
    private AirplaneEntity airplane;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
