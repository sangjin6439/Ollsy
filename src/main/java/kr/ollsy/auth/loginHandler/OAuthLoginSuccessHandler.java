package kr.ollsy.auth.loginHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ollsy.auth.info.GoogleUserInfo;
import kr.ollsy.auth.info.KakaoUserInfo;
import kr.ollsy.auth.info.NaverUserInfo;
import kr.ollsy.auth.info.OAuth2UserInfo;
import kr.ollsy.auth.jwt.entity.RefreshToken;
import kr.ollsy.auth.jwt.filter.JwtUtil;
import kr.ollsy.auth.jwt.repository.RefreshTokenRepository;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.global.util.NicknameGenerator;
import kr.ollsy.user.domain.Role;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${jwt.redirect}")
    private String redirectUri; // 프론트엔드로 Jwt 토큰을 리다이렉트할 URI

    @Value("${jwt.access-token.expiration-time}")
    private long accessTokenExpirationTime; // 액세스 토큰 만료 시간

    @Value("${jwt.refresh-token.expiration-time}")
    private long refreshTokenExpirationTime; // 리프레쉬 토큰 만료 시간

    @Value("${admin.email}")
    private String adminEmail;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰
        final String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

        OAuth2UserInfo oAuth2UserInfo = extractUserInfo(token, provider);
        User user = userProcess(oAuth2UserInfo, provider);

        // 리프레쉬 토큰 발급 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), refreshTokenExpirationTime);
        RefreshToken newRefreshToken = generateNewRefreshToken(user, refreshToken);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId(), accessTokenExpirationTime);

        //유저 정보 및 토큰을 담아 리다이렉트
        sendRedirect(response, user, oAuth2UserInfo, refreshToken, accessToken);
    }

    private User userProcess(OAuth2UserInfo oAuth2UserInfo, String provider) {

        Optional<User> existUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;

        if (!existUser.isPresent()) {
            // 신규 유저인 경우
            log.info("신규 유저 등록을 진행합니다.");

            user = createUser(provider, oAuth2UserInfo);
        } else {
            user = existUser.get();
            // 기존 유저인 경우
            log.info("기존 유저입니다.");

            if (!existUser.get().getProvider().equals(provider)) {
                log.error("이미 다른 소셜 계정으로 등록된 유저입니다.");
                throw new CustomException(GlobalExceptionCode.DUPLICATE_EMAIL);
            }

            refreshTokenRepository.deleteByUserId(existUser.get().getId());
        }
        return user;
    }

    private User createUser(String provider, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .name(oAuth2UserInfo.getName())
                .nickname(NicknameGenerator.generateNickname())
                .email(oAuth2UserInfo.getEmail())
                .role(oAuth2UserInfo.getEmail().equals(adminEmail) ? Role.ADMIN : Role.USER)
                .provider(provider)
                .providerId(oAuth2UserInfo.getProviderId())
                .build();
        userRepository.save(user);
        return user;
    }

    private OAuth2UserInfo extractUserInfo(OAuth2AuthenticationToken token, String provider) {
        // 구글 || 카카오 || 네이버 로그인 요청
        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                return new GoogleUserInfo(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                return new KakaoUserInfo(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                return new NaverUserInfo((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }
        return null;
    }

    private RefreshToken generateNewRefreshToken(User user, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);
        return newRefreshToken;
    }

    private void sendRedirect(HttpServletResponse response, User user, OAuth2UserInfo oAuth2UserInfo, String refreshToken, String accessToken) throws IOException {
        {
            String redirectUrl = "/oauth2-redirect.html" +
                    "?accessToken=" + accessToken +
                    "&refreshToken=" + refreshToken +
                    "&userId=" + user.getId() +
                    "&userName=" + URLEncoder.encode(oAuth2UserInfo.getName(), StandardCharsets.UTF_8) +
                    "&email=" + URLEncoder.encode(oAuth2UserInfo.getEmail(), StandardCharsets.UTF_8);

            response.sendRedirect(redirectUrl);
        }
    }
}