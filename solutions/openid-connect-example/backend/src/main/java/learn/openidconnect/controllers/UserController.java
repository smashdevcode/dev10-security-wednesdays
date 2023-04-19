package learn.openidconnect.controllers;

import learn.openidconnect.models.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/oauth")
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User principal) {
        // It's not a great idea to return a whole OAuth2User in an endpoint
        // since it might contain information you would rather not reveal to a browser client.
        return principal;
    }

    @GetMapping("/oidc")
    public OidcUser getUser(@AuthenticationPrincipal OidcUser principal) {
        // It's not a great idea to return a whole OAuth2User in an endpoint
        // since it might contain information you would rather not reveal to a browser client.

        // OidcIdToken token = principal.getIdToken();

        return principal;
    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal AppUser principal) {
        if (principal.getAppUserId() > 0) {
            return new ResponseEntity<>(principal.serialize(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name")
    public Map<String, Object> getUserName(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            request.logout();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ServletException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
