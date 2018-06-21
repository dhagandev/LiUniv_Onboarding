package liuni;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.StatusUpdate;
import twitter4j.Status;


public class TwitterStatus {

    private static final int TWITTER_CHAR_MAX = 280;

    public void postStatus(String text) throws Exception {
        Twitter twitter = TwitterFactory.getSingleton();
        StatusUpdate newStatus = new StatusUpdate(text);
        Status status = twitter.updateStatus(newStatus);
        System.out.println("Successfully updated status to [" + status.getText() + "].");
    }

    // True = No errors; False = Error occurred
    public boolean textErrorCheck(String text) {
        char[] convertedArr = text.toCharArray();
        int statusLength = convertedArr.length;
        if (statusLength > TWITTER_CHAR_MAX || text.equals("")) {
            return false;
        }

        return true;
    }

}
