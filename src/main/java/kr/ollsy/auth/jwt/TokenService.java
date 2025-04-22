package kr.ollsy.auth.jwt;

import kr.ollsy.auth.jwt.dto.TokenResponse;

public interface TokenService {
    TokenResponse reissueAccessToken(String authorizationHeader);
}
