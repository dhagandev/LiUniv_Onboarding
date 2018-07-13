package liuni.configs;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class TwitterUserConfig {
    @NotEmpty
    private String consumerKey;

    @NotEmpty
    private String consumerSecret;

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String accessSecret;

    @JsonProperty("consumerKey")
    public String getConsumerKey() {
        return consumerKey;
    }

    @JsonProperty("consumerSecret")
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("accessSecret")
    public String getAccessSecret() {
        return accessSecret;
    }

    @JsonProperty("consumerKey")
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @JsonProperty("consumerSecret")
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @JsonProperty("accessToken")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("accessSecret")
    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

}
