package learn.openidconnect.data;

import learn.openidconnect.models.AppRole;
import learn.openidconnect.models.AppUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AppUserRepository {
    private final static List<AppUser> users = new ArrayList<>(List.of(
        new AppUser(1, new AppRole(1, "user"), "James Churchill", "james@smashdev.com", "smashdevcode")
    ));

    public AppUser getByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    public AppUser getByGitHubUsername(String gitHubUsername) {
        return users.stream()
                .filter(u -> u.getGitHubUsername().equalsIgnoreCase(gitHubUsername))
                .findFirst().orElse(null);
    }
}
