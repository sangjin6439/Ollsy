package kr.ollsy.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        String nickname) {
}
