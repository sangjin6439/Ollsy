package kr.ollsy.Auth.Jwt;

import kr.ollsy.Auth.Jwt.dto.TokenResponse;

public interface TokenService {

    TokenResponse reissueAccessToken(String authorizationHeader);
}
