package liuni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.Status;

public class TwitterStatus {

    public static final int TWITTER_CHAR_MAX = 280;

    private static Logger logger = LoggerFactory.getLogger(TwitterStatus.class);

    private Twitter twitter;

    public TwitterStatus(TwitterConfig config) {
        if (config != null) {
            twitter = config.createTwitterConfig();
        }
        else {
            twitter = null;
        }
    }

    public boolean postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            Status status = twitter.updateStatus(text);
            logger.info("Successfully updated status to [" + status.getText() + "].");
        }
        return isOkToPost;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
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
