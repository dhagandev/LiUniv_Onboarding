package liuni;

import liuni.resources.LiUniResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class MockitoTest {

    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;
    @InjectMocks TwitterTimeline twitterTimeline;
    @InjectMocks LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* Test LiUniResource .postTweet() REST method */
    @Test
    public void testREST_postTweet_successfulPost() {
//        resource = new LiUniResource();
//        twitterStatus = new TwitterStatus();
//        twitterStatus.setTwitter(twitter);
//        boolean testResult;
//        try {
//            when(twitterStatus.postStatus(("message"))).thenReturn(true);
//            Response resp = resource.postTweet("message");
//            testResult = resp.getStatus() == Response.Status.CREATED.getStatusCode();
//        }
//        catch (Exception e) {
//            testResult = false;
//        }
//
//        assertTrue(testResult);
    }

    /* Test TwitterTimeline .getTimeline() method */
    @Test
    public void testTwitterTimeline_Empty() {
        TwitterTimeline twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
//                    when(twitter.getHomeTimeline()).thenReturn(statuses);
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testList = twitterTimeline.getTimeline();
            testResult = testList.size() == 0;
        }
        catch (Exception e) {
            testResult = false;
        }
        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testTwitterTimeline_Update() {
        TwitterTimeline twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testResultList = twitterTimeline.getTimeline();
            testResult = testResultList.size() == 0;
            assertTrue(testResult);

            respList.add(status);
            respList.size();

            testResultList = twitterTimeline.getTimeline();
            testResult = testResultList.size() == 1;
        }
        catch (Exception e) {
            testResult = false;
        }
        assertTrue(testResult);
        reset(twitter);
        reset(status);
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
    public void testTweetPost_DoubleTweet() {
        String testString = "This is a test post for a double post error.";
        boolean testResult;
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);
            testResult = postTestString(testString);
            assertTrue(testResult);

            when(twitter.updateStatus(testString)).thenThrow(new Exception());
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

    private class ResponseListImpl<T> extends ArrayList<T> implements ResponseList<T> {
        private static final long serialVersionUID = 9105950888010803544L;
        private transient RateLimitStatus rateLimitStatus = null;
        private transient int accessLevel;

        @Override
        public RateLimitStatus getRateLimitStatus() {
            return rateLimitStatus;
        }

        @Override
        public int getAccessLevel() {
            return accessLevel;
        }
    }
}
