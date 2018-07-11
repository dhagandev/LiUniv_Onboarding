package liuni;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LiUniConfig extends Configuration {
    private List<TwitterConfig> twitter;

    @JsonProperty("twitter")
    public List<TwitterConfig> getTwitter() {
        return twitter;
    }

    @JsonProperty("twitter")
    public void setTwitter(List<TwitterConfig> twitter) {
        this.twitter = twitter;
    }


}
