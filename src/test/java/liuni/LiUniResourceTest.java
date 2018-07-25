package liuni;

import liuni.models.ErrorModel;
import liuni.configs.TwitterConfig;
import liuni.configs.TwitterAccountConfig;
import liuni.models.TwitterTweetModel;
import liuni.resources.LiUniResource;
import liuni.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LiUniResourceTest {
    private TwitterService mockedService;
    private TwitterConfig mockedConfig;
    private LiUniResource resource;

    @Before
    public void setUp() {
        mockedService = mock(TwitterService.class);
        mockedConfig = mock(TwitterConfig.class);
        when(mockedConfig.getDefaultAccountIndex()).thenReturn(0);
        when(mockedConfig.getTwitterAccounts()).thenReturn(new ArrayList<TwitterAccountConfig>());

        resource = new LiUniResource(mockedConfig);
        resource.setTwitterService(mockedService);
    }

    @Test
    public void testRestFetchTimelineEmptyTimeline() {
        try {
            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            when(mockedService.getTimeline()).thenReturn(Optional.of(expected));
            Response resp = resource.fetchTimeline();
            List<TwitterTweetModel> result = (List<TwitterTweetModel>) resp.getEntity();
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFetchTimelineUpdateTimeline() {
        try {
            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            when(mockedService.getTimeline()).thenReturn(Optional.of(expected));

            Response resp = resource.fetchTimeline();
            List<TwitterTweetModel> result = (List<TwitterTweetModel>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);

            TwitterTweetModel tweetModel = mock(TwitterTweetModel.class);
            expected.add(tweetModel);
            when(mockedService.getTimeline()).thenReturn(Optional.of(expected));

            resp = resource.fetchTimeline();
            result = (List<TwitterTweetModel>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFetchTimelineTimelineException() {
        try {
            ErrorModel expected = new ErrorModel();
            expected.setError(ErrorModel.ErrorType.GENERAL);
            when(mockedService.getTimeline()).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.fetchTimeline();
            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFilterTweetsEmptyResult() {
        try {
            String filterKey = "New";
            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            when(mockedService.getFiltered(filterKey)).thenReturn(Optional.of(expected));

            Response resp = resource.filterTweets(filterKey);
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
        try {
            String filterKey = "New";
            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            expected.add(mock(TwitterTweetModel.class));
            expected.add(mock(TwitterTweetModel.class));
            when(mockedService.getFiltered(filterKey)).thenReturn(Optional.of(expected));

            Response resp = resource.filterTweets(filterKey);
            List<String> result = (List<String>) resp.getEntity();

            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestFilterTweetsException() {
        try {
            String filterKey = "New";
            ErrorModel expected = new ErrorModel();
            expected.setError(ErrorModel.ErrorType.GENERAL);
            when(mockedService.getFiltered(filterKey)).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.filterTweets(filterKey);
            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetSuccessfulPost() {
        try {
            String message = "Test tweet";
            TwitterTweetModel expected = mock(TwitterTweetModel.class);
            when(mockedService.postStatus(message)).thenReturn(Optional.of(expected));

            Response resp = resource.postTweet(message);
            TwitterTweetModel result = (TwitterTweetModel) resp.getEntity();

            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetException() {
        try {
            String message = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);
            ErrorModel expected = new ErrorModel();
            expected.setError(ErrorModel.ErrorType.GENERAL);
            when(mockedService.postStatus(message)).thenThrow(new TwitterException("This is an exception test."));

            Response resp = resource.postTweet(message);
            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testRestPostTweetBadTweet() {
        try {
            String message = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);
            ErrorModel expected = new ErrorModel();
            expected.setError(ErrorModel.ErrorType.BAD_TWEET);
            when(mockedService.postStatus(message)).thenReturn(Optional.empty());

            Response resp = resource.postTweet(message);
            ErrorModel result = (ErrorModel) resp.getEntity();

            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testGetSetConfig() {
        TwitterConfig retrievedConfig = resource.getConfig();
        TwitterConfig mockedTwitterConfig = mock(TwitterConfig.class);
        resource.setConfig(mockedTwitterConfig);
        assertNotEquals(retrievedConfig, mockedTwitterConfig);
    }

    @Test
    public void testSetConfigIndex() {
        int newIndex = 1;

        assertNotEquals(newIndex, resource.getDefaultAccountIndex());

        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
        accConfigList.add(mock(TwitterAccountConfig.class));
        accConfigList.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);

        resource.setConfig(mockedConfig);
        resource.setConfigIndex(newIndex);

        assertEquals(newIndex, resource.getDefaultAccountIndex());
    }

    @Test
    public void testSetConfigIndexOutofBoundsHigh() {
        int newIndex = 1;

        assertNotEquals(newIndex, resource.getDefaultAccountIndex());

        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
        accConfigList.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);

        resource.setConfig(mockedConfig);
        resource.setConfigIndex(newIndex);

        assertNotEquals(newIndex, resource.getDefaultAccountIndex());
    }

    @Test
    public void testSetConfigIndexOutofBoundLow() {
        int newIndex = -1;

        assertNotEquals(newIndex, resource.getDefaultAccountIndex());

        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
        accConfigList.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);

        resource.setConfig(mockedConfig);
        resource.setConfigIndex(newIndex);

        assertNotEquals(newIndex, resource.getDefaultAccountIndex());
    }

    @Test
    public void testConfigNull() {
        resource = new LiUniResource(null);
        assertEquals(null, resource.getConfig());
        assertEquals(null, resource.getTwitterService());
    }

    @Test
    public void testConfigListNotEmptyIndexHigh() {
        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
        when(mockedConfig.getDefaultAccountIndex()).thenReturn(mockedAccConfigs.size()+1);

        resource = new LiUniResource(mockedConfig);

        assert(resource.getDefaultAccountIndex() >= mockedAccConfigs.size());
        assertEquals(null, resource.getTwitterService());
    }

    @Test
    public void testConfigListNotEmptyIndexLow() {
        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
        when(mockedConfig.getDefaultAccountIndex()).thenReturn(-1);

        resource = new LiUniResource(mockedConfig);

        assert(resource.getDefaultAccountIndex() < 0);
        assertEquals(null, resource.getTwitterService());
    }

    @Test
    public void testConfigListNotEmptyIndexAppropriate() {
        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
        when(mockedConfig.getDefaultAccountIndex()).thenReturn(0);

        resource = new LiUniResource(mockedConfig);

        assert(resource.getDefaultAccountIndex() >= 0);
        assert(resource.getDefaultAccountIndex() < mockedAccConfigs.size());
        assertNotEquals(null, resource.getTwitterService());
    }

}
