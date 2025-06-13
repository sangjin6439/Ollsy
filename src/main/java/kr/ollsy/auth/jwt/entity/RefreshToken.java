package kr.ollsy.auth.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_tokens_id")
    private Long id;

    @Column(name = "users_id", unique = true)
    private Long userId;

    @Column(name = "token", nullable = false)
    private String token;
}