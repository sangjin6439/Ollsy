package kr.ollsy.auth.jwt.service;

import kr.ollsy.auth.jwt.dto.TokenResponse;

public interface TokenService {
    TokenResponse reissueAccessToken(String authorizationHeader);
}
