package learn.openidconnect.security;

import learn.openidconnect.data.AppUserRepository;
import learn.openidconnect.models.AppUser;
import learn.openidconnect.models.CustomOidcUser;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final AppUserRepository appUserRepository;

    public CustomOidcUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        CustomOidcUser customOidcUser = new CustomOidcUser(oidcUser);

        AppUser appUser = appUserRepository.getByEmail(oidcUser.getEmail());
        if (appUser != null) {
            customOidcUser.initialize(appUser);
        }

        return customOidcUser;
    }
}
