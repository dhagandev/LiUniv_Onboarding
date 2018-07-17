package liuni;

import liuni.models.ErrorModel;
import liuni.configs.TwitterConfig;
import liuni.configs.TwitterAccountConfig;
import liuni.models.TwitterTweetModel;
import liuni.resources.LiUniResource;
import liuni.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.ws.rs.core.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
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
        List<TwitterAccountConfig> list = new ArrayList<TwitterAccountConfig>();
        TwitterAccountConfig twitterAccountConfig1 = mock(TwitterAccountConfig.class);
        TwitterAccountConfig twitterAccountConfig2 = mock(TwitterAccountConfig.class);
        when(twitterAccountConfig1.getConsumerKey()).thenReturn("TestKey1");
        when(twitterAccountConfig2.getConsumerKey()).thenReturn("TestKey2");
        list.add(twitterAccountConfig1);
        list.add(twitterAccountConfig2);
        when(twitterConfig.getTwitterAccounts()).thenReturn(list);
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
    public void testRestFetchTimelineEmptyTimeline() {
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        try {
            TwitterService twitterService = mock(TwitterService.class);
            when(twitterService.getTimeline()).thenReturn(tweetModelList);
            resource.setTwitterService(twitterService);

            Response resp = resource.fetchTimeline();

            List<TwitterTweetModel> result = (List<TwitterTweetModel>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(tweetModelList, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFetchTimelineUpdateTimeline() {
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        try {
            TwitterService twitterService = mock(TwitterService.class);
            when(twitterService.getTimeline()).thenReturn(tweetModelList);
            resource.setTwitterService(twitterService);

            Response resp = resource.fetchTimeline();

            assertEquals(resp.getStatus(), Response.Status.OK.getStatusCode());
            assertEquals(tweetModelList, resp.getEntity());

            tweetModelList.add(mock(TwitterTweetModel.class));
            resp = resource.fetchTimeline();

            List<TwitterTweetModel> result = (List<TwitterTweetModel>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(tweetModelList, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFetchTimelineTimelineException() {
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        tweetModelList.add(mock(TwitterTweetModel.class));
        ErrorModel errorModel = new ErrorModel();
        errorModel.setError("");
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.fetchTimeline();

            ErrorModel resultError = (ErrorModel) resp.getEntity();

            assertEquals(errorModel, resultError);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    /* Test LiUniResource .postTweet() REST method */
    @Test
    public void testRestPostTweetSuccessfulPostGoodURL() {
        String testMessage = "message";
        String testName = "MockedUserName";
        String testScreenName = "MockedUserScreenName";
        String testURL = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
        Date date = new Date(2323223232L);
        try {
            URL testAsURL = new URL(testURL);
            when(twitter.updateStatus(testMessage)).thenReturn(status);
            User mockedUser = mock(User.class);
            when(mockedUser.getName()).thenReturn(testName);
            when(mockedUser.getScreenName()).thenReturn(testScreenName);
            when(mockedUser.getProfileImageURL()).thenReturn(testURL);
            when(status.getUser()).thenReturn(mockedUser);
            when(status.getText()).thenReturn(testMessage);
            when(status.getCreatedAt()).thenReturn(date);

            Response resp = resource.postTweet(testMessage);

            TwitterTweetModel result = (TwitterTweetModel) resp.getEntity();

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(testMessage, result.getMessage());
            assertEquals(date, result.getCreatedAt());
            assertEquals(testName, result.getUser().getName());
            assertEquals(testScreenName, result.getUser().getTwitterHandle());
            assertEquals(testAsURL, result.getUser().getProfileImageUrl());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetSuccessfulPostBadURL() {
        String testMessage = "message";
        String testName = "MockedUserName";
        String testScreenName = "MockedUserScreenName";
        String testURL = "MockedURL";
        Date date = new Date(2323223232L);
        try {
            URL testAsURL = null;
            when(twitter.updateStatus(testMessage)).thenReturn(status);
            User mockedUser = mock(User.class);
            when(mockedUser.getName()).thenReturn(testName);
            when(mockedUser.getScreenName()).thenReturn(testScreenName);
            when(mockedUser.getProfileImageURL()).thenReturn(testURL);
            when(status.getUser()).thenReturn(mockedUser);
            when(status.getText()).thenReturn(testMessage);
            when(status.getCreatedAt()).thenReturn(date);

            Response resp = resource.postTweet(testMessage);

            TwitterTweetModel result = (TwitterTweetModel) resp.getEntity();

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(testMessage, result.getMessage());
            assertEquals(date, result.getCreatedAt());
            assertEquals(testName, result.getUser().getName());
            assertEquals(testScreenName, result.getUser().getTwitterHandle());
            assertEquals(testAsURL, result.getUser().getProfileImageUrl());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetBadPostBadTweet() {
        String testString = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);
            ErrorModel expected = new ErrorModel();
            expected.setError("BAD_TWEET");

            Response resp = resource.postTweet(testString);

            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetBadPostEmptyTweet() {
        String testString = "";
        try {
            when(twitter.updateStatus(testString)).thenReturn(status);
            when(status.getText()).thenReturn(testString);
            ErrorModel expected = new ErrorModel();
            expected.setError("BAD_TWEET");

            Response resp = resource.postTweet(testString);

            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetBadPostGeneralError() {
        String testString = "Bad Message; Exception testing.";
        try {
            when(twitter.updateStatus(testString)).thenThrow(new TwitterException("This is an exception test."));
            ErrorModel expected = new ErrorModel();
            expected.setError("");

            Response resp = resource.postTweet(testString);

            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetPostDifferentUsers() {
        TwitterConfig twitterConfig = resource.getConfig();
        assertTrue(twitterConfig.getTwitterAccounts().size() > 1);

        String conKey1 = resource.getTwitterService().getConfig().getConsumerKey();

        resource.setConfigIndex(1);
        String conKey2 = resource.getTwitterService().getConfig().getConsumerKey();
        assertNotEquals(conKey1, conKey2);
    }

    @Test
    public void testRestTwitterValConfigSize() {
        TwitterConfig config = mock(TwitterConfig.class);
        List<TwitterAccountConfig> twitterConfigList = new ArrayList<TwitterAccountConfig>();
        when(config.getTwitterAccounts()).thenReturn(twitterConfigList);
        assertEquals(0, twitterConfigList.size());

        resource = new LiUniResource(config);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testRestTwitterValIndexOutOfBounds() {
        TwitterConfig config = mock(TwitterConfig.class);
        TwitterAccountConfig twitterConfig = mock(TwitterAccountConfig.class);
        List<TwitterAccountConfig> twitterConfigList = new ArrayList<TwitterAccountConfig>();
        twitterConfigList.add(twitterConfig);
        when(config.getTwitterAccounts()).thenReturn(twitterConfigList);
        when(config.getDefaultAccountIndex()).thenReturn(-1);

        resource = new LiUniResource(config);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());

        when(config.getDefaultAccountIndex()).thenReturn(twitterConfigList.size()+1);
        resource = new LiUniResource(config);
        resource.setTwitterService(service);
        res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertNotEquals(null, resource.getConfig());
    }

    @Test
    public void testRestTwitterValConfigNull() {
        resource = new LiUniResource(null);
        TwitterService service = mock(TwitterService.class);
        resource.setTwitterService(service);
        TwitterService res = resource.getTwitterService();
        verify(res, never()).createTwitter();
        assertEquals(null, resource.getConfig());
    }

    @Test
    public void testSetConfigSuccess() {
        TwitterConfig mock = resource.getConfig();
        resource.setConfig(mock);
        assertEquals(mock, resource.getConfig());
    }

    @Test
    public void testSetConfigIndexFailIndexLow() {
        TwitterConfig twitterService = resource.getConfig();
        List<TwitterAccountConfig> list = new ArrayList<TwitterAccountConfig>();
        when(twitterService.getTwitterAccounts()).thenReturn(list);
        resource.setConfigIndex(-1);

        assertNotEquals(-1, resource.getDefaultAccountIndex());
    }

    @Test
    public void testSetConfigIndexFailIndexHigh() {
        TwitterConfig twitterService = resource.getConfig();
        List<TwitterAccountConfig> list = new ArrayList<TwitterAccountConfig>();
        when(twitterService.getTwitterAccounts()).thenReturn(list);
        resource.setConfigIndex(list.size()+1);

        assertNotEquals(list.size()+1, resource.getDefaultAccountIndex());
    }
}
