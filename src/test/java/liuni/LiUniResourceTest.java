package liuni;

import liuni.api.ErrorModel;
import liuni.resources.LiUniResource;
import liuni.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LiUniResourceTest {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        LiUniConfig config = mock(LiUniConfig.class);
        List<TwitterConfig> twitterConfigList = new ArrayList<TwitterConfig>();
        TwitterConfig twitterConfig1 = mock(TwitterConfig.class);
        TwitterConfig twitterConfig2 = mock(TwitterConfig.class);

        when(twitterConfig1.getUserName()).thenReturn("Dev1");
        when(twitterConfig2.getUserName()).thenReturn("Dev2");

        twitterConfigList.add(twitterConfig1);
        twitterConfigList.add(twitterConfig2);

        when(config.getTwitter()).thenReturn(twitterConfigList);
        twitter = mock(Twitter.class);

        resource = new LiUniResource(config, 0);
        resource.getTwitterService().setTwitter(twitter);
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
        String testString = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);
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
    public void testREST_postTweet_badPost_emptyTweet() {
        String testString = "";
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

    @Test
    public void testREST_postTweet_postDifferentUsers() {
        LiUniConfig luConfig = resource.getConfig();
        assertTrue(luConfig.getTwitter().size() > 1);

        String userName1 = resource.getTwitterService().getConfig().getUserName();

        resource.setTwitterConfig(1);
        String userName2 = resource.getTwitterService().getConfig().getUserName();
        assertNotEquals(userName1, userName2);
    }

    @Test
    public void testREST_twitterVal_configSize() {
        LiUniConfig config = mock(LiUniConfig.class);
        List<TwitterConfig> twitterConfigList = new ArrayList<TwitterConfig>();
        when(config.getTwitter()).thenReturn(twitterConfigList);
        assertEquals(0, twitterConfigList.size());

        resource = new LiUniResource(config, 0);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testREST_twitterVal_indexOutOfBounds() {
        LiUniConfig config = mock(LiUniConfig.class);
        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        List<TwitterConfig> twitterConfigList = new ArrayList<TwitterConfig>();
        twitterConfigList.add(twitterConfig);
        when(config.getTwitter()).thenReturn(twitterConfigList);
        int index = -1;

        resource = new LiUniResource(config, index);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testREST_twitterVal_configNull() {
        resource = new LiUniResource(null, 0);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertEquals(null, resource.getConfig());
    }

}
