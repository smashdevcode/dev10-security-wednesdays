package learn.openidconnect.security;

import learn.openidconnect.data.AppUserRepository;
import learn.openidconnect.models.AppUser;
import learn.openidconnect.models.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AppUserRepository appUserRepository;

    public CustomOAuth2UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User);

        AppUser appUser = appUserRepository.getByGitHubUsername(oAuth2User.getAttribute("login"));
        if (appUser != null) {
            customOAuth2User.initialize(appUser);
        }

        return customOAuth2User;
    }
}
