package dding.auth.auth.Oauth2;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}
