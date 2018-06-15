package liuni;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.StatusUpdate;
import twitter4j.Status;


public class TwitterStatus {

    private static final int TWITTER_CHAR_MAX = 280;

    public void postStatus(String text) {
        Twitter twitter = TwitterFactory.getSingleton();
        if (textErrorCheck(text)) {
            StatusUpdate newStatus = new StatusUpdate(text);
            try {
                Status status = twitter.updateStatus(newStatus);
                System.out.println("Successfully updated status to [" + status.getText() + "].");
            }
            catch (TwitterException e) {
                System.out.println("Exception occurred. " + e.getErrorMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
        else {
            System.out.println("\n\nAn error occurred. Unable to post your tweet [" + text + "]. Sorry!");
        }
    }

    // True = No errors; False = Error occurred
    private boolean textErrorCheck(String text) {
        char[] convertedArr = text.toCharArray();

        //Twitter shortens URLs to TWITTER_URL_LEN characters
        int statusLength = convertedArr.length;

        if (statusLength > TWITTER_CHAR_MAX) {
            return false;
        }

        return true;
    }

}
