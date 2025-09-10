package kr.artner.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
    GOOGLE("google"),
    KAKAO("kakao");

    private final String value;

    public static OAuthProvider fromValue(String value) {
        for (OAuthProvider provider : values()) {
            if (provider.getValue().equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown OAuth Provider: " + value);
    }
}