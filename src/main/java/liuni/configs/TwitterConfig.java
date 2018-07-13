package liuni.configs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TwitterConfig {
    private int defaultAccountIndex;
    private List<TwitterAccountConfig> twitterAccounts;

    @JsonProperty("defaultAccountIndex")
    public int getDefaultAccountIndex() {
        return defaultAccountIndex;
    }

    @JsonProperty("defaultAccountIndex")
    public void setDefaultAccountIndex(int defaultAccountIndex) {
        this.defaultAccountIndex = defaultAccountIndex;
    }

    @JsonProperty("accounts")
    public List<TwitterAccountConfig> getTwitterAccounts() {
        return twitterAccounts;
    }

    @JsonProperty("accounts")
    public void setTwitterAccounts(List<TwitterAccountConfig> twitterAccounts) {
        this.twitterAccounts = twitterAccounts;
    }
}
