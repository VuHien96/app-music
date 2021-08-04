package com.vuhien.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by VuHien96 on 01/08/2021 19:27
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "verification_token")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token")
    private String token;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
