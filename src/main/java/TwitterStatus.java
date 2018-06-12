import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.StatusUpdate;
import twitter4j.Status;

import java.net.URL;

public class TwitterStatus {

    private static final int TWITTER_CHAR_MAX = 280;
    private static final int TWITTER_URL_LEN = 22;
    private static Twitter twitter = TwitterFactory.getSingleton();

    public void postStatus(String text) {
        if (textErrorCheck(text)) {
            StatusUpdate newStatus = new StatusUpdate(text);
            try {
                Status status = twitter.updateStatus(newStatus);
                System.out.println("Successfully updated status to [" + status.getText() + "].");
            }
            catch (TwitterException e) {
                System.out.println("Exception occurred. " + e.getErrorMessage());
                System.out.println(e.getExceptionCode());
            }
        }
    }

    // True = No errors; False = Error occurred
    private boolean textErrorCheck(String text) {
        char[] convertedArr = text.toCharArray();

        //Twitter shortens URLs to TWITTER_URL_LEN characters
        boolean containsURL = statusContainsUrl(text);
        int statusLength = convertedArr.length;
        if (containsURL) {
            statusLength -= TWITTER_URL_LEN;
        }

        if (statusLength <= TWITTER_CHAR_MAX) {
            return false;
        }

        return true;
    }

    private static boolean statusContainsUrl(String text) {
        //Find possible URL, check validity through isValidUrl
        return false; //Temporary value
    }

    private static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
