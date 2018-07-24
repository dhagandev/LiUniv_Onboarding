package liuni;

import liuni.models.ErrorModel;
import liuni.configs.TwitterConfig;
import liuni.configs.TwitterAccountConfig;
import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
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

    public Status createMockedStatus(Date testDate, String testMessage, String urlString, String testScreenName, String testName) {
        Status status = mock(Status.class);

        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(testName);
        when(mockUser.getScreenName()).thenReturn(testScreenName);
        when(mockUser.getProfileImageURL()).thenReturn(urlString);
        when(status.getUser()).thenReturn(mockUser);
        when(status.getText()).thenReturn(testMessage);
        when(status.getCreatedAt()).thenReturn(testDate);

        return status;
    }

    public TwitterTweetModel createTweetModel(Date testDate, String testMessage, URL url, String testScreenName, String testName) {
        TwitterTweetModel tweetModel = new TwitterTweetModel();
        tweetModel.setCreatedAt(testDate);
        tweetModel.setMessage(testMessage);

        UserModel user = new UserModel();
        user.setProfileImageUrl(url);
        user.setTwitterHandle(testScreenName);
        user.setName(testName);

        tweetModel.setUser(user);

        return tweetModel;
    }

    /* Test LiUniResource .fetchTimeline() REST method */
    @Test
    public void testRestFetchTimelineEmptyTimeline() {
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        ResponseList<Status> mockedStatuses = new ResponseListImpl<Status>();
        try {
            twitter = mock(Twitter.class);
            when(twitter.getHomeTimeline()).thenReturn(mockedStatuses);
            resource.getTwitterService().setTwitter(twitter);

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
        ResponseList<Status> mockedStatuses = new ResponseListImpl<>();
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        try {
            twitter = mock(Twitter.class);
            when(twitter.getHomeTimeline()).thenReturn(mockedStatuses);
            resource.getTwitterService().setTwitter(twitter);

            Response resp = resource.fetchTimeline();

            List<TwitterTweetModel> result = (List<TwitterTweetModel>) resp.getEntity();

            assertEquals(resp.getStatus(), Response.Status.OK.getStatusCode());
            assertEquals(tweetModelList, result);


            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String urlString = "";
            URL testProfileUrl = null;
            String testTweetMessage = "Model Message";
            Date testDate = new Date(2323223232L);

            mockedStatuses.add(createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName));
            tweetModelList.add(createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName));
            resp = resource.fetchTimeline();

            result = (List<TwitterTweetModel>) resp.getEntity();

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
        errorModel.setError(ErrorModel.ErrorType.GENERAL);
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

    /* Test LiUniResource .filterTweets() REST method */
    @Test
    public void testRestFilterTweetsEmptyResult() {
        ResponseList<Status> mockedStatuses = new ResponseListImpl<>();
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        String testTweetMessage = "Old";
        Date testDate = new Date(2323223232L);

        mockedStatuses.add(createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName));

        List<String> expected = new ArrayList<String>();
        try {
            twitter = mock(Twitter.class);
            when(twitter.getHomeTimeline()).thenReturn(mockedStatuses);
            resource.getTwitterService().setTwitter(twitter);

            Response resp = resource.filterTweets("New");

            List<String> result = (List<String>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFilterTweetsNotEmptyResult() {
        ResponseList<Status> mockedStatuses = new ResponseListImpl<>();
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        String testTweetMessage = "Test Tweet";
        Date testDate = new Date(2323223232L);

        mockedStatuses.add(createMockedStatus(testDate, testTweetMessage + " new", urlString, testScreenName, testName));
        mockedStatuses.add(createMockedStatus(testDate, testTweetMessage + " old", urlString, testScreenName, testName));
        mockedStatuses.add(createMockedStatus(testDate, testTweetMessage + " Newt", urlString, testScreenName, testName));

        List<String> expected = new ArrayList<String>();
        expected.add("Test Tweet new");
        expected.add("Test Tweet Newt");
        try {
            twitter = mock(Twitter.class);
            when(twitter.getHomeTimeline()).thenReturn(mockedStatuses);
            resource.getTwitterService().setTwitter(twitter);

            Response resp = resource.filterTweets("New");

            List<String> result = (List<String>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFilterTweetsTimelineException() {
        List<TwitterTweetModel> tweetModelList = new ArrayList<TwitterTweetModel>();
        tweetModelList.add(mock(TwitterTweetModel.class));
        ErrorModel errorModel = new ErrorModel();
        errorModel.setError(ErrorModel.ErrorType.GENERAL);
        try {
            when(twitter.getHomeTimeline()).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.filterTweets("Thing");

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
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
        String testTweetMessage = "test message";
        Date testDate = new Date(2323223232L);
        try {
            URL testAsURL = new URL(urlString);
            Status mockedStatus = createMockedStatus(testDate,testTweetMessage,urlString,testScreenName,testName);
            TwitterTweetModel expected = createTweetModel(testDate,testTweetMessage,testAsURL,testScreenName,testName);

            twitter = mock(Twitter.class);
            when(twitter.updateStatus(testTweetMessage)).thenReturn(mockedStatus);
            resource.getTwitterService().setTwitter(twitter);

            Response resp = resource.postTweet(testTweetMessage);

            TwitterTweetModel result = (TwitterTweetModel) resp.getEntity();

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetSuccessfulPostBadURL() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        String testTweetMessage = "test message";
        Date testDate = new Date(2323223232L);
        try {
            URL testAsURL = null;
            Status mockedStatus = createMockedStatus(testDate,testTweetMessage,urlString,testScreenName,testName);
            TwitterTweetModel expected = createTweetModel(testDate,testTweetMessage,testAsURL,testScreenName,testName);

            twitter = mock(Twitter.class);
            when(twitter.updateStatus(testTweetMessage)).thenReturn(mockedStatus);
            resource.getTwitterService().setTwitter(twitter);

            Response resp = resource.postTweet(testTweetMessage);

            TwitterTweetModel result = (TwitterTweetModel) resp.getEntity();

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
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
            expected.setError(ErrorModel.ErrorType.BAD_TWEET);

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
            expected.setError(ErrorModel.ErrorType.BAD_TWEET);

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
            expected.setError(ErrorModel.ErrorType.GENERAL);

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
