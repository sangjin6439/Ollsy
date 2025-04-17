package kr.ollsy.auth.jwt.dto;

import kr.ollsy.auth.info.OAuth2UserInfo;
import kr.ollsy.user.domain.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserInfo oAuth2UserInfo;
    private final User user;

    public OAuth2UserInfo getOAuth2UserInfo() {
        return oAuth2UserInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getName() {
        return oAuth2UserInfo.getProviderId();
    }
}