package kr.ollsy.auth.info;

import java.util.Map;

import kr.ollsy.auth.info.OAuth2UserInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        //Long -> String 타입 변환
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }
}
