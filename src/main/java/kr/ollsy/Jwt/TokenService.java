package kr.ollsy.Jwt;

import kr.ollsy.Jwt.dto.TokenResponse;

public interface TokenService {

    TokenResponse reissueAccessToken(String authorizationHeader);
}
