package me.manaki.deepup.security.oauth.factory;

import me.manaki.deepup.security.oauth.GoogleOAuth2UserInfo;
import me.manaki.deepup.security.oauth.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) return new GoogleOAuth2UserInfo(attributes);

        return null;
    }

}
