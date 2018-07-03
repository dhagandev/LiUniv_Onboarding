package liuni;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
