package liuni;

import liuni.api.ErrorModel;
import liuni.configs.TwitterConfig;
import liuni.configs.TwitterUserConfig;
import liuni.resources.LiUniResource;
import liuni.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LiUniResourceTest {
    @Mock private Twitter twitter;
    @Mock private Status status;

    private LiUniResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        List<TwitterUserConfig> list = new ArrayList<TwitterUserConfig>();
        TwitterUserConfig twitterUserConfig1 = mock(TwitterUserConfig.class);
        TwitterUserConfig twitterUserConfig2 = mock(TwitterUserConfig.class);
        when(twitterUserConfig1.getConsumerKey()).thenReturn("TestKey1");
        when(twitterUserConfig2.getConsumerKey()).thenReturn("TestKey2");
        list.add(twitterUserConfig1);
        list.add(twitterUserConfig2);
        when(twitterConfig.getTwitterUsers()).thenReturn(list);
        twitter = mock(Twitter.class);

        resource = new LiUniResource(twitterConfig);
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
        TwitterConfig twitterConfig = resource.getConfig();
        assertTrue(twitterConfig.getTwitterUsers().size() > 1);

        String conKey1 = resource.getTwitterService().getConfig().getConsumerKey();

        resource.setConfigIndex(1);
        String conKey2 = resource.getTwitterService().getConfig().getConsumerKey();
        assertNotEquals(conKey1, conKey2);
    }

    @Test
    public void testREST_twitterVal_configSize() {
        TwitterConfig config = mock(TwitterConfig.class);
        List<TwitterUserConfig> twitterConfigList = new ArrayList<TwitterUserConfig>();
        when(config.getTwitterUsers()).thenReturn(twitterConfigList);
        assertEquals(0, twitterConfigList.size());

        resource = new LiUniResource(config);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testREST_twitterVal_indexOutOfBounds() {
        TwitterConfig config = mock(TwitterConfig.class);
        TwitterUserConfig twitterConfig = mock(TwitterUserConfig.class);
        List<TwitterUserConfig> twitterConfigList = new ArrayList<TwitterUserConfig>();
        twitterConfigList.add(twitterConfig);
        when(config.getTwitterUsers()).thenReturn(twitterConfigList);
        when(config.getDefaultUser()).thenReturn(-1);

        resource = new LiUniResource(config);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());

        when(config.getDefaultUser()).thenReturn(twitterConfigList.size()+1);
        resource = new LiUniResource(config);
        resource.setTwitterService(service);
        res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testREST_twitterVal_configNull() {
        resource = new LiUniResource(null);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertEquals(null, resource.getConfig());
    }

    @Test
    public void test_SetConfig() {
        TwitterConfig mock = resource.getConfig();
        resource.setConfig(mock);
        assertEquals(mock, resource.getConfig());
    }
}
