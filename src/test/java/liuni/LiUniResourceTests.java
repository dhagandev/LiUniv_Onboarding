package liuni;

import liuni.api.ErrorModel;
import liuni.resources.LiUniResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class LiUniResourceTests {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;
    @InjectMocks TwitterTimeline twitterTimeline;
    @InjectMocks LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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

}
