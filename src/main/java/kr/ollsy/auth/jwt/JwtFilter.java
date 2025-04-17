package kr.ollsy.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ollsy.auth.jwt.dto.CustomOAuth2User;
import kr.ollsy.global.dto.ExceptionResponse;
import kr.ollsy.global.exception.CustomException;
import kr.ollsy.global.exception.GlobalExceptionCode;
import kr.ollsy.user.domain.User;
import kr.ollsy.user.domain.UserOAuth2UserInfo;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private static final String JWT_HEADER_KEY = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveTokenFromRequest(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }
            if (!jwtUtil.isTokenExpired(token)) {
                Long userId = Long.valueOf(jwtUtil.getUserIdFromToken(token));

                User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(GlobalExceptionCode.USER_NOT_FOUND));

                UserOAuth2UserInfo userInfo = new UserOAuth2UserInfo(user);
                CustomOAuth2User principal = new CustomOAuth2User(userInfo, user);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e){
            log.error("JwtFilter Error: {}", e.getErrorMessage());

            response.setStatus(e.getHttpStatus().value());
            response.setContentType("application/json;charset=UTF-8");

            ExceptionResponse exceptionResponse = ExceptionResponse.of(e.getErrorCode(),e.getErrorMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(exceptionResponse);
            response.getWriter().write(json);
        }
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(JWT_HEADER_KEY);

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
