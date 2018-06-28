package liuni;

import org.junit.After;
import org.junit.Assert;
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

public class TwitterStatusTest {
    private static final int TWITTER_CHAR_MAX = 280;

    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
    }

    @After
    public void tearDown() {
        reset(twitter);
        reset(status);
    }

    /* Test TwitterStatus .postStatus() method */
    @Test
    public void testTweetPost_GoodTweet() {
        String testString = "This is a good test post.";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            assertTrue(twitterStatus.postStatus(testString));
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testTweetPost_EmptyTweet() {
        String testString = "";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            assertFalse(twitterStatus.postStatus(testString));
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testTweetPost_TooLongTweet() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < TWITTER_CHAR_MAX + 1; i ++){
            stringBuffer.append("a");
        }
        String testString = stringBuffer.toString();
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            assertFalse(twitterStatus.postStatus(testString));
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }
}
