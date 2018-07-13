package liuni.configs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TwitterConfig {
    private int defaultUser;
    private List<TwitterUserConfig> twitterUsers;

    @JsonProperty("defaultUserIndex")
    public int getDefaultUser() {
        return defaultUser;
    }

    @JsonProperty("defaultUserIndex")
    public void setDefaultUser(int defaultUser) {
        this.defaultUser = defaultUser;
    }

    @JsonProperty("users")
    public List<TwitterUserConfig> getTwitterUsers() {
        return twitterUsers;
    }

    @JsonProperty("users")
    public void setTwitterUsers(List<TwitterUserConfig> twitterUsers) {
        this.twitterUsers = twitterUsers;
    }
}
