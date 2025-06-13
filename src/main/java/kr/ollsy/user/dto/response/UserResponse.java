package kr.ollsy.user.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        String nickname) {
}
