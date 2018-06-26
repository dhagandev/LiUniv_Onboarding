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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class TwitterStatusTests {
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

            assertEquals(twitterStatus.postStatus(testString), true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testTweetPost_EmptyTweet() {
        String testString = "";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            assertEquals(twitterStatus.postStatus(testString), false);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testTweetPost_TooLongTweet() {
        String testString = "This is a bad string because it is going go to be longer than Twitter's limits. It is this long because it is Lorem Ipsum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vehicula velit vel augue porta condimentum. Nullam ornare velit mattis, maximus sem eget, malesuada nibh. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse potenti. Pellentesque a dictum lorem. Ut sit amet fringilla turpis. Sed pretium, erat tincidunt aliquam mattis, neque diam luctus mauris, sed lacinia arcu libero sit amet tortor.";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            assertEquals(twitterStatus.postStatus(testString), false);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }
}
