package kr.ollsy.user.domain;

import kr.ollsy.auth.OAuth2UserInfo;
import kr.ollsy.user.domain.User;

public class UserOAuth2UserInfo implements OAuth2UserInfo {

    private final User user;

    public UserOAuth2UserInfo(User user) {
        this.user = user;
    }

    @Override
    public String getProviderId() {
        return user.getProviderId();
    }

    @Override
    public String getProvider() {
        return user.getProvider();
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }
}