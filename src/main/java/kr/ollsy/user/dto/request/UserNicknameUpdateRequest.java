package kr.ollsy.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserNicknameUpdateRequest(
        @NotNull(message = "닉네임을 입력해 주세요.")
        @Size(message = "최소 2 글자에서 최대 8 글자를 입력해 주세요.", min = 2, max = 8)
        String nickname) {
}