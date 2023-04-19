package learn.openidconnect.models;

public class Stuff {
    private int appUserId;
    private String description;

    public Stuff(int appUserId, String description) {
        this.appUserId = appUserId;
        this.description = description;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
