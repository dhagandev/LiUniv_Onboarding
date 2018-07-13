package liuni.configs;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LiUniConfig extends Configuration {
    private TwitterConfig twitter;

    @JsonProperty("twitter")
    public TwitterConfig getTwitter() {
        return twitter;
    }

    @JsonProperty("twitter")
    public void setTwitter(TwitterConfig twitter) {
        this.twitter = twitter;
    }


}
