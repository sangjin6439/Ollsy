package kr.ollsy.auth.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ollsy.auth.OAuth2UserInfo;
import kr.ollsy.auth.OAuthLoginSuccessHandler;
import kr.ollsy.auth.jwt.dto.CustomOAuth2User;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.domain.UserOAuth2UserInfo;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final String JWT_HEADER_KEY = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenFromRequest(request);
        if(token ==null){
            filterChain.doFilter(request,response);
            return;
        }
        if(!jwtUtil.isTokenExpired(token)){
            Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));

            User user = userRepository.findById(userId).orElseThrow(()-> new CustomException(GlobalExceptionCode.NOT_FOUND));

            UserOAuth2UserInfo userInfo = new UserOAuth2UserInfo(user);
            CustomOAuth2User principal = new CustomOAuth2User(userInfo);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal,null,null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(JWT_HEADER_KEY);

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
