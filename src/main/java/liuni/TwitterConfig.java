package liuni;

import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.validation.constraints.NotNull;

public class TwitterConfig extends io.dropwizard.Configuration {
    @NotNull
    private Map<String, String> twitter;

    @NotEmpty
    private String consumerKey = "consumerKey";

    @NotEmpty
    private String consumerSecret = "consumerSecret";

    @NotEmpty
    private String accessToken = "accessToken";

    @NotEmpty
    private String accessSecret = "accessSecret";

    @JsonProperty
    public String getConsumerKey() {
        return twitter.get(consumerKey);
    }

    @JsonProperty
    public String getConsumerSecret() {
        return twitter.get(consumerSecret);
    }

    @JsonProperty
    public String getAccessToken() {
        return twitter.get(accessToken);
    }

    @JsonProperty
    public String getAccessSecret() {
        return twitter.get(accessSecret);
    }

    @JsonProperty
    public Map<String, String> getTwitter() {
        return twitter;
    }

    @JsonProperty
    public void setConsumerKey(String consumerKey) {
        twitter.replace(this.consumerKey, consumerKey);
    }

    @JsonProperty
    public void setConsumerSecret(String consumerSecret) {
        twitter.replace(this.consumerSecret, consumerSecret);
    }

    @JsonProperty
    public void setAccessToken(String accessToken) {
        twitter.replace(this.accessToken, accessToken);
    }

    @JsonProperty
    public void setAccessSecret(String accessSecret) {
        twitter.replace(this.accessSecret, accessSecret);
    }

    @JsonProperty
    public void setTwitter(Map<String, String> twitter) {
        this.twitter = twitter;
    }

    public Twitter createTwitterConfig() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(getConsumerKey());
        cb.setOAuthConsumerSecret(getConsumerSecret());
        cb.setOAuthAccessToken(getAccessToken());
        cb.setOAuthAccessTokenSecret(getAccessSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        Twitter twitterObj = twitterFactory.getInstance();

        return twitterObj;
    }
}
