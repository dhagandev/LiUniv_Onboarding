package liuni;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Status;
import twitter4j.Twitter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class TwitterStatusTests {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* Test TwitterStatus .postStatus() method */
    @Test
    public void testTweetPost_GoodTweet() {
        String testString = "This is a good test post.";
        boolean testResult;
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            testResult = postTestString(testString);
        }
        catch (Exception e) {
            testResult = false;
        }
        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testTweetPost_EmptyTweet() {
        String testString = "";
        boolean testResult;
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            testResult = postTestString(testString);
        }
        catch (Exception e) {
            testResult = false;
        }
        assertFalse(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testTweetPost_TooLongTweet() {
        String testString = "This is a bad string because it is going go to be longer than Twitter's limits. It is this long because it is Lorem Ipsum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vehicula velit vel augue porta condimentum. Nullam ornare velit mattis, maximus sem eget, malesuada nibh. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse potenti. Pellentesque a dictum lorem. Ut sit amet fringilla turpis. Sed pretium, erat tincidunt aliquam mattis, neque diam luctus mauris, sed lacinia arcu libero sit amet tortor.";
        boolean testResult;
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            testResult = postTestString(testString);
        }
        catch (Exception e) {
            testResult = false;
        }
        assertFalse(testResult);
        reset(twitter);
        reset(status);
    }

    private boolean postTestString(String testString) {
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        try {
            boolean tweetPosted = twitterStatus.postStatus(testString);
            return tweetPosted;
        }
        catch (Exception e) {
            return false;
        }
    }
}
