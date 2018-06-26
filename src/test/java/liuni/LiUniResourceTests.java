package liuni;

import liuni.api.ErrorModel;
import liuni.resources.LiUniResource;
import org.junit.After;
import org.junit.Assert;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class LiUniResourceTests {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;
    @InjectMocks LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);
    }

    @After
    public void tearDown() {
        reset(twitter);
        reset(status);
    }

    /* Test LiUniResource .fetchTimeline() REST method */
    @Test
    public void testREST_fetchTimeline_emptyTimeline() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            boolean statusCheck = resp.getStatus() == Response.Status.OK.getStatusCode();
            boolean entityCheck = resp.hasEntity() && resp.getEntity().equals(respList);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testREST_fetchTimeline_updateTimeline() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();
            boolean statusCheck = resp.getStatus() == Response.Status.OK.getStatusCode();
            boolean entityCheck  = resp.hasEntity() && resp.getEntity().equals(respList);
            assertEquals(statusCheck && entityCheck, true);

            respList.add(status);
            resp = resource.fetchTimeline();
            statusCheck = resp.getStatus() == Response.Status.OK.getStatusCode();
            entityCheck = resp.hasEntity() && resp.getEntity().equals(respList);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testREST_fetchTimeline_fetchTimelineError() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        respList.add(status);
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException(""));

            Response resp = resource.fetchTimeline();
            boolean statusCheck = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            boolean entityCheck = resp.getEntity().toString().equals(expectedError);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    /* Test LiUniResource .postTweet() REST method */
    @Test
    public void testREST_postTweet_successfulPost() {
        String testString = "message";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            boolean statusCheck = resp.getStatus() == Response.Status.CREATED.getStatusCode();
            boolean entityCheck = resp.getEntity().toString().equals(testString);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testREST_postTweet_badPost_badTweet() {
        String testString = "";
        String expectedError = (new ErrorModel()).getBadTweetError();
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);
            boolean statusCheck = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            boolean entityCheck = resp.getEntity().toString().equals(expectedError);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testREST_postTweet_badPost_generalError() {
        String testString = "Bad Message; Exception testing.";
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.updateStatus(testString)).thenThrow(new TwitterException(""));

            Response resp = resource.postTweet(testString);
            boolean statusCheck = resp.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            boolean entityCheck = resp.getEntity().toString().equals(expectedError);

            assertEquals(statusCheck, true);
            assertEquals(entityCheck, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

}
