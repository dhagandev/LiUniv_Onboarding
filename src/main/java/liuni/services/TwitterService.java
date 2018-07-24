package liuni.services;

import liuni.configs.TwitterAccountConfig;
import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TwitterService {
    public final static int TWITTER_CHAR_MAX = 280;
    private final static Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private static TwitterService INSTANCE = null;

    private TwitterAccountConfig twitterAccountConfig;
    private Twitter twitter;

    private TwitterService() {
        twitterAccountConfig = null;
        twitter = null;
    }

    public static TwitterService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TwitterService();
        }
        return INSTANCE;
    }

    public void createTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(twitterAccountConfig.getConsumerKey());
        cb.setOAuthConsumerSecret(twitterAccountConfig.getConsumerSecret());
        cb.setOAuthAccessToken(twitterAccountConfig.getAccessToken());
        cb.setOAuthAccessTokenSecret(twitterAccountConfig.getAccessSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }

    public TwitterAccountConfig getConfig() {
        return twitterAccountConfig;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public void setTwitterAccountConfig(TwitterAccountConfig config) {
        this.twitterAccountConfig = config;
        createTwitter();
    }

    public Optional<TwitterTweetModel> postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            Optional<TwitterTweetModel> tweet = Optional.of(Stream.of(twitter.updateStatus(text))
                                        .peek(status -> logger.info("Successfully updated status to [" + status.getText() + "]."))
                                        .map(status -> getTweet(status))
                                        .findFirst()
                                        .get());
            return tweet;
        }
        return Optional.empty();
    }

    public Optional<List<TwitterTweetModel>> getTimeline() throws TwitterException {
        return Optional.of(twitter.getHomeTimeline().stream()
               .map(status -> getTweet(status))
               .collect(Collectors.toCollection(() -> new ArrayList<TwitterTweetModel>())));
    }

    public Optional<List<String>> getFiltered(String filterKey) throws TwitterException {
        return Optional.of(twitter.getHomeTimeline().stream()
                                  .filter(status -> status.getText().toLowerCase().contains(filterKey.toLowerCase()))
                                  .map(status -> status.getText())
                                  .collect(Collectors.toCollection(() -> new ArrayList<String>())));
    }

    public TwitterTweetModel getTweet(Status status) {
        TwitterTweetModel tweet = new TwitterTweetModel();
        UserModel user = getUser(status);

        tweet.setMessage(status.getText());
        tweet.setUser(user);
        tweet.setCreatedAt(status.getCreatedAt());

        return tweet;
    }

    public UserModel getUser(Status status) {
        UserModel user = new UserModel();
        User twitterUser = status.getUser();

        user.setName(twitterUser.getName());
        user.setTwitterHandle(twitterUser.getScreenName());
        URL url = null;
        try {
            url = new URL(twitterUser.getProfileImageURL());
        }
        catch (MalformedURLException e) {
            logger.error("Improper URL:", e);
        }
        user.setProfileImageUrl(url);
        return user;
    }

    // True = No errors; False = Error occurred
    private boolean textErrorCheck(String text) {
        int statusLength = text.length();
        if (statusLength > TWITTER_CHAR_MAX || text.equals("")) {
            return false;
        }

        return true;
    }
}
