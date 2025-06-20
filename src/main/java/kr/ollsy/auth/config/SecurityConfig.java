package kr.ollsy.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import kr.ollsy.auth.jwt.filter.JwtFilter;
import kr.ollsy.auth.jwt.filter.JwtUtil;
import kr.ollsy.auth.loginHandler.OAuthLoginFailureHandler;
import kr.ollsy.auth.loginHandler.OAuthLoginSuccessHandler;
import kr.ollsy.global.exception.CustomAccessDeniedHandler;
import kr.ollsy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final OAuthLoginFailureHandler oAuthLoginFailureHandler;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // 허용할 origin
            config.setAllowCredentials(true);
            return config;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                httpBasic(HttpBasicConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // X-Frames-Options 비활성화 -> h2 데이터베이스 콘솔 사용 가능
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/test","/h2-console/**","/swagger-ui/**","/swagger-ui.html","/v3/api-docs/**","/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/v1/item/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1/category/**","/api/v1/item/**","/api/v1/itemImage/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH,"/api/v1/item/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/category/**","/api/v1/item/**","/api/v1/itemImage/**").hasRole("ADMIN")
                                .requestMatchers("/api/v1/order/**","/api/v1/user/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        oauth
                                .successHandler(oAuthLoginSuccessHandler) // 로그인 성공 시 핸들러
                                .failureHandler(oAuthLoginFailureHandler) // 로그인 실패 시 핸들러
                )
                .addFilterBefore(new JwtFilter(jwtUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(eh-> eh.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                );;
        return httpSecurity.build();
    }
}
