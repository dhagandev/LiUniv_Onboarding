package liuni;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Status;

public class TwitterStatus {

    private static final int TWITTER_CHAR_MAX = 280;
    private static Twitter twitter;

    public TwitterStatus() {
        twitter = TwitterFactory.getSingleton();
    }

    public boolean postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            Status status = twitter.updateStatus(text);
            System.out.println("Successfully updated status to [" + status.getText() + "].");
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
