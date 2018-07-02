package liuni;

import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;

public class TwitterConfig extends io.dropwizard.Configuration {
    @NotEmpty
    private String consumerKey;

    @NotEmpty
    private String consumerSecret;

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String accessSecret;

    @JsonProperty
    public String getConsumerKey() {
        return consumerKey;
    }

    @JsonProperty
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @JsonProperty
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty
    public String getAccessSecret() {
        return accessSecret;
    }

    @JsonProperty
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @JsonProperty
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @JsonProperty
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty
    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public Twitter createTwitterConfig() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        Twitter twitter = twitterFactory.getInstance();

        return twitter;
    }
}
