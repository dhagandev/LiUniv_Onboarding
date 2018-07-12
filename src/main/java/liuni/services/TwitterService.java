package liuni.services;

import liuni.TwitterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterService {
    private final static TwitterService INSTANCE = new TwitterService();
    private static Logger logger = LoggerFactory.getLogger(TwitterService.class);

    private TwitterConfig twitterConfig;
    private Twitter twitter;

    public static final int TWITTER_CHAR_MAX = 280;

    private TwitterService() {
        twitterConfig = null;
        twitter = null;
    }

    public static TwitterService getInstance() {
        return INSTANCE;
    }

    public void createTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(twitterConfig.getConsumerKey());
        cb.setOAuthConsumerSecret(twitterConfig.getConsumerSecret());
        cb.setOAuthAccessToken(twitterConfig.getAccessToken());
        cb.setOAuthAccessTokenSecret(twitterConfig.getAccessSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }

    public TwitterConfig getConfig() {
        return twitterConfig;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public void setTwitterConfig(TwitterConfig config) {
        this.twitterConfig = config;
        createTwitter();
    }

    public ResponseList<Status> getTimeline() throws TwitterException {
        ResponseList<Status> statuses = twitter.getHomeTimeline();
        return statuses;
    }

    public boolean postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            Status status = twitter.updateStatus(text);
            logger.info("Successfully updated status to [" + status.getText() + "].");
        }
        return isOkToPost;
    }

    // True = No errors; False = Error occurred
    private boolean textErrorCheck(String text) {
        char[] convertedArr = text.toCharArray();
        int statusLength = convertedArr.length;
        if (statusLength > TWITTER_CHAR_MAX || text.equals("")) {
            return false;
        }

        return true;
    }
}
