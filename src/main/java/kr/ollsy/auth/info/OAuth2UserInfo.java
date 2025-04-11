package kr.ollsy.auth.info;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
}
