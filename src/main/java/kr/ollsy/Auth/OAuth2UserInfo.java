package kr.ollsy.Auth;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}
