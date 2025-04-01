package kr.ollsy.user.domain;

import jakarta.persistence.*;
import kr.ollsy.user.dto.request.UserNicknameUpdateRequest;
import lombok.AccessLevel;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column(name = "name", nullable = false) //이런 규칙들은 엔티티가 변경될 수 있는 dto로 다 옮기고 entity에 혼잡을 주지 않기
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "provider", nullable = false, length = 10)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}