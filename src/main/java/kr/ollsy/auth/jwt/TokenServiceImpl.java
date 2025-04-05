package kr.ollsy.auth.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.ollsy.auth.jwt.dto.TokenResponse;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    @Value("${jwt.access-token.expiration-time}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public TokenResponse reissueAccessToken(final String authorizationHeader) {
        String refreshToken = jwtUtil.getTokenFromHeader(authorizationHeader);
        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        RefreshToken existRefreshToken = refreshTokenRepository.findByUserId(Long.valueOf(userId));
        String accessToken = null;

        //리프레쉬 토큰이 다르거나, 만료된 경우
        if (!existRefreshToken.getToken().equals(refreshToken) || jwtUtil.isTokenExpired(refreshToken)) {
            throw new CustomException(GlobalExceptionCode.UNAUTHORIZED);
        } else {
            accessToken = jwtUtil.generateAccessToken(Long.valueOf(userId), ACCESS_TOKEN_EXPIRATION_TIME);
        }

        return TokenResponse.of(accessToken);
    }
}
