package learn.openidconnect.models;

public class AppRole {
    private int appRoleId;
    private String name;

    public AppRole(int appRoleId, String name) {
        this.appRoleId = appRoleId;
        this.name = name;
    }

    public int getAppRoleId() {
        return appRoleId;
    }

    public void setAppRoleId(int appRoleId) {
        this.appRoleId = appRoleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
