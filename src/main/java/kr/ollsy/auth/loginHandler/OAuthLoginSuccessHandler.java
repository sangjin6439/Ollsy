package kr.ollsy.auth.loginHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import kr.ollsy.auth.jwt.JwtUtil;
import kr.ollsy.auth.jwt.RefreshToken;
import kr.ollsy.auth.jwt.RefreshTokenRepository;
import kr.ollsy.auth.jwt.dto.LoginResponse;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.global.exception.GlobalExceptionHandler;
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

    private OAuth2UserInfo oAuth2UserInfo = null;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰
        final String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

        // 구글 || 카카오 || 네이버 로그인 요청
        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
                System.out.print(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getEmail();

        Optional<User> existUser = userRepository.findByEmail(email);
        User user;

        if (!existUser.isPresent()) {
            // 신규 유저인 경우
            log.info("신규 유저입니다. 등록을 진행합니다.");

            user = User.builder()
                    .name(name)
                    .nickname(NicknameGenerator.generateNickname())
                    .email(email)
                    .role(email.equals(adminEmail) ? Role.ADMIN : Role.USER)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else {
            // 기존 유저인 경우
            log.info("기존 유저입니다.");

            if (!existUser.get().getProvider().equals(provider)) {
                log.error("이미 다른 소셜 계정으로 등록된 유저입니다.");
                throw new CustomException(GlobalExceptionCode.DUPLICATE_EMAIL);
            }

            refreshTokenRepository.deleteByUserId(existUser.get().getId());
            user = existUser.get();
        }

        log.info("유저 이름 : {}", name);
        log.info("유저 이름 : {}", email);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

        // 리프레쉬 토큰 발급 후 저장
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), refreshTokenExpirationTime);

        RefreshToken newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshToken)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId(), accessTokenExpirationTime);

        //유저 정보 및 토큰을 담아 리다이렉트
        String redirectUrl = "/oauth2-redirect.html" +
                "?accessToken=" + accessToken +
                "&refreshToken=" + refreshToken +
                "&userId=" + user.getId() +
                "&userName=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
                "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }
}