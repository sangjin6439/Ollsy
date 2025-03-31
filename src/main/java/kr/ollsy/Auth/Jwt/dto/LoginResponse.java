package kr.ollsy.Auth.Jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public class LoginResponse {

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("user_id")
    private Long user_id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("refreshToken")
    private String refreshToken;

    public static LoginResponse of(String userName, Long user_id,String email, String accessToken, String refreshToken) {
        return LoginResponse.builder()
                .userName(userName)
                .user_id(user_id)
                .email(email)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
