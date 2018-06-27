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

public class LiUniResourceTest {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterStatus twitterStatus;
    @InjectMocks TwitterTimeline twitterTimeline;
    @InjectMocks LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twitterStatus.setTwitter(twitter);
        resource.setTwitterStatus(twitterStatus);
        twitterTimeline.setTwitter(twitter);
        resource.setTwitterTimeline(twitterTimeline);
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

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(respList, resp.getEntity());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testREST_fetchTimeline_updateTimeline() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            Response resp = resource.fetchTimeline();

            assertEquals(resp.getStatus(), Response.Status.OK.getStatusCode());
            assertEquals(resp.getEntity(), respList);

            respList.add(status);
            resp = resource.fetchTimeline();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(respList, resp.getEntity());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testREST_fetchTimeline_fetchTimelineError() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        respList.add(status);
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.fetchTimeline();

            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
            assertEquals(expectedError, resp.getEntity().toString());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
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

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(testString, resp.getEntity().toString());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testREST_postTweet_badPost_badTweet() {
        String testString = "This is a bad string because it is going go to be longer than Twitter's limits. It is this long because it is Lorem Ipsum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In vehicula velit vel augue porta condimentum. Nullam ornare velit mattis, maximus sem eget, malesuada nibh. Interdum et malesuada fames ac ante ipsum primis in faucibus. Suspendisse potenti. Pellentesque a dictum lorem. Ut sit amet fringilla turpis. Sed pretium, erat tincidunt aliquam mattis, neque diam luctus mauris, sed lacinia arcu libero sit amet tortor.";
        String expectedError = (new ErrorModel()).getBadTweetError();
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);

            Response resp = resource.postTweet(testString);

            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
            assertEquals(expectedError, resp.getEntity().toString());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testREST_postTweet_badPost_generalError() {
        String testString = "Bad Message; Exception testing.";
        String expectedError = (new ErrorModel()).getGeneralError();
        try {
            when(twitter.updateStatus(testString)).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.postTweet(testString);

            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
            assertEquals(expectedError, resp.getEntity().toString()) ;
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

}
