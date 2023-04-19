package learn.openidconnect.models;

import java.util.HashMap;
import java.util.Map;

public class AppUser {
    private int appUserId;
    private AppRole role;
    private String name;
    private String email;
    private String gitHubUsername;

    public AppUser() { }

    public AppUser(int appUserId, AppRole role, String name, String email, String gitHubUsername) {
        this.appUserId = appUserId;
        this.role = role;
        this.name = name;
        this.email = email;
        this.gitHubUsername = gitHubUsername;
    }

    public void initialize(AppUser appUser) {
        setAppUserId(appUser.getAppUserId());
        setRole(appUser.getRole());
        setName(appUser.getName());
        setEmail(appUser.getEmail());
        setGitHubUsername(appUser.getGitHubUsername());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("appUserId", appUserId);
        map.put("role", role);
        map.put("name", name);
        map.put("email", email);
        map.put("gitHubUsername", gitHubUsername);
        return map;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public AppRole getRole() {
        return role;
    }

    public void setRole(AppRole role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGitHubUsername() {
        return gitHubUsername;
    }

    public void setGitHubUsername(String gitHubUsername) {
        this.gitHubUsername = gitHubUsername;
    }
}
