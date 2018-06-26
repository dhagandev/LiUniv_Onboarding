package liuni;

import liuni.api.ErrorModel;
import liuni.resources.LiUniResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Element;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    @Mock private Element element;

    @InjectMocks TwitterStatus twitterStatus;
    @InjectMocks TwitterTimeline twitterTimeline;
    @InjectMocks LiUniResource resource;
    @InjectMocks KeyHandler keyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* Test KeyHandler .setupKeys() method */
    @Test
    public void testSetupKeys_HCKeysExist() {
        File HCKeysFile = new File("hardcoded_keys.xml");
        assertTrue(HCKeysFile.exists());
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Success() {
        File twitterPropFile = new File("target/twitter4j.properties");
        assertFalse(twitterPropFile.exists());

        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);

        keyHandler.setupKeys();
        assertTrue(twitterPropFile.exists());
        twitterPropFile.delete();
    }

    /* Test LiUniResource .fetchTimeline() REST method */
    @Test
    public void testREST_fetchTimeline_emptyTimeline_Status() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            testResult = resp.getStatus() == Response.Status.OK.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_fetchTimeline_emptyTimeline_Entity() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            testResult = resp.hasEntity() && resp.getEntity().equals(respList);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_fetchTimeline_updateTimeline_Status() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            testResult = resp.getStatus() == Response.Status.OK.getStatusCode();
            assertTrue(testResult);

            respList.add(status);
            resp = resource.fetchTimeline();
            testResult = resp.getStatus() == Response.Status.OK.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_fetchTimeline_updateTimeline_Entity() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            testResult = resp.hasEntity() && resp.getEntity().equals(respList);
            assertTrue(testResult);

            respList.add(status);
            resp = resource.fetchTimeline();
            testResult = resp.hasEntity() && resp.getEntity().equals(respList);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_fetchTimeline_fetchTimelineError_Status() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        respList.add(status);
        boolean testResult;
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException(""));

            Response resp = resource.fetchTimeline();
            testResult = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_fetchTimeline_fetchTimelineError_Entity() {
        resource = new LiUniResource();
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);

        ResponseList<Status> respList = new ResponseListImpl<Status>();
        respList.add(status);
        boolean testResult;
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException(""));

            Response resp = resource.fetchTimeline();
            testResult = resp.getEntity().toString().equals(expectedError);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    /* Test LiUniResource .postTweet() REST method */
    @Test
    public void testREST_postTweet_successfulPost_Status() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "message";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            testResult = resp.getStatus() == Response.Status.CREATED.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_postTweet_successfulPost_Entity() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "message";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            testResult = resp.getEntity().toString().equals(testString);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_postTweet_badPost_badTweetStatus() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            testResult = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_postTweet_badPost_badTweetEntity() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "";
        String expectedError = (new ErrorModel()).getBadTweetError();
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            testResult = resp.getEntity().toString().equals(expectedError);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_postTweet_badPost_generalErrorStatus() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "Bad Message; Exception testing.";
        try {
            when(twitter.updateStatus(testString)).thenThrow(new TwitterException(""));

            Response resp = resource.postTweet(testString);
            testResult = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testREST_postTweet_badPost_generalErrorEntity() {
        resource = new LiUniResource();
        twitterStatus = new TwitterStatus();
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);

        boolean testResult;
        String testString = "Bad Message; Exception testing.";
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.updateStatus(testString)).thenThrow(new TwitterException(""));

            Response resp = resource.postTweet(testString);
            testResult = resp.getEntity().toString().equals(expectedError);
        }
        catch (Exception e) {
            testResult = false;
        }

        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    /* Test TwitterTimeline .getTimeline() method */
    @Test
    public void testTwitterTimeline_Empty() {
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
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
        twitterTimeline = new TwitterTimeline();
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
