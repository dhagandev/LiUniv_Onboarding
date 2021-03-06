package liuni;

import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import liuni.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterServiceTest {
    private TwitterService twitterService;
    private Twitter twitter;
    private TimeCache mockedCache;

    @Before
    public void setUp() {
        twitter = mock(Twitter.class);
        mockedCache = mock(TimeCache.class);
        when(mockedCache.getEntry(isA(String.class))).thenReturn(null);
        twitterService = new TwitterService(twitter);
        twitterService.setCache(mockedCache);
    }

    public TwitterTweetModel createTweetModel(Date testDate, String testMessage, URL url, String testScreenName, String testName, URL link) {
        TwitterTweetModel tweetModel = new TwitterTweetModel();
        UserModel user = createUserModel(url, testScreenName, testName);
        tweetModel.setUser(user);
        tweetModel.setCreatedAt(testDate);
        tweetModel.setMessage(testMessage);
        tweetModel.setLink(link);

        return tweetModel;
    }

    public UserModel createUserModel(URL url, String testScreenName, String testName) {
        UserModel user = new UserModel();
        user.setProfileImageUrl(url);
        user.setTwitterHandle(testScreenName);
        user.setName(testName);

        return user;
    }

    public Status createMockedStatus(Date testDate, String testMessage, String urlString, String testScreenName, String testName, long id) {
        Status status = mock(Status.class);
        User user = createdMockedUser(urlString, testScreenName, testName);
        when(status.getUser()).thenReturn(user);
        when(status.getText()).thenReturn(testMessage);
        when(status.getCreatedAt()).thenReturn(testDate);
        when(status.getId()).thenReturn(id);

        return status;
    }

    public User createdMockedUser(String urlString, String testScreenName, String testName) {
        User mockUser = mock(User.class);
        when(mockUser.getName()).thenReturn(testName);
        when(mockUser.getScreenName()).thenReturn(testScreenName);
        when(mockUser.getProfileImageURL()).thenReturn(urlString);

        return mockUser;
    }

    @Test
    public void testGetTweet() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);
        String urlString = "";
        URL testProfileUrl = null;

        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName, null);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);

        TwitterTweetModel result = twitterService.getTweet(status);
        assertEquals(expected, result);
    }

    @Test
    public void testGetTweetBadURL() {
        String testName = "";
        String testScreenName = "";
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);
        String urlString = "";
        URL testProfileUrl = null;

        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName, null);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);

        twitterService.setUrl("");
        TwitterTweetModel result = twitterService.getTweet(status);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUserBadURL() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);
        String urlString = "";
        URL testProfileUrl = null;

        UserModel expected = createUserModel(testProfileUrl, testScreenName, testName);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);

        UserModel result = twitterService.getUser(status);
        assertEquals(expected, result);
    }

    @Test
    public void testGetUserGoodURL() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);
        String urlString = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
        URL testProfileUrl = null;
        try {
            testProfileUrl = new URL(urlString);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }

        UserModel expected = createUserModel(testProfileUrl, testScreenName, testName);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);

        UserModel result = twitterService.getUser(status);
        assertEquals(expected, result);
    }

    @Test
    public void testGetTimeline() {
        try {
            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String testTweetMessage = "Model Message";
            Date testDate = new Date(2323223232L);
            String urlString = "";
            URL testProfileUrl = null;

            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            ResponseList<Status> responseList = new ResponseListImpl<Status>();

            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel1);
            Status status1 = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);
            responseList.add(status1);

            TwitterTweetModel tweetModel2 = createTweetModel(testDate, testTweetMessage + " add on", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel2);
            Status status2 = createMockedStatus(testDate, testTweetMessage + " add on", urlString, testScreenName, testName, 0);
            responseList.add(status2);

            when(twitter.getHomeTimeline()).thenReturn(responseList);
            when(mockedCache.putEntry(isA(String.class), isA(List.class))).thenReturn(expected);

            List<TwitterTweetModel> result = twitterService.getTimeline().get();
            assertTrue(expected.size() > 0);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), result.get(i));
            }

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testGetTimelineCached() {
        try {
            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String testTweetMessage = "Model Message";
            Date testDate = new Date(2323223232L);
            URL testProfileUrl = null;

            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();

            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel1);

            TwitterTweetModel tweetModel2 = createTweetModel(testDate, testTweetMessage + " add on", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel2);

            when(mockedCache.getEntry(isA(String.class))).thenReturn(expected);
            twitterService.setCache(mockedCache);

            List<TwitterTweetModel> result = twitterService.getTimeline().get();
            assertTrue(expected.size() > 0);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), result.get(i));
            }

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testGetFiltered() {
        try {
            String filterKey = "new";

            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String testTweetMessage = "Model Message";
            Date testDate = new Date(2323223232L);
            String urlString = "";
            URL testProfileUrl = null;

            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            ResponseList<Status> responseList = new ResponseListImpl<Status>();

            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage + " NEWNEWNEW", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel1);
            Status status1 = createMockedStatus(testDate, testTweetMessage + " NEWNEWNEW", urlString, testScreenName, testName, 0);
            responseList.add(status1);

            Status status2 = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);
            responseList.add(status2);

            TwitterTweetModel tweetModel3 = createTweetModel(testDate, testTweetMessage + " new", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel3);
            Status status3 = createMockedStatus(testDate, testTweetMessage + " new", urlString, testScreenName, testName, 0);
            responseList.add(status3);

            when(twitter.getHomeTimeline()).thenReturn(responseList);
            when(mockedCache.putEntry(isA(String.class), isA(List.class))).thenReturn(expected);

            List<TwitterTweetModel> result = twitterService.getFiltered(filterKey).get();
            assertTrue(expected.size() > 0);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), result.get(i));
            }

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testGetFilteredCache() {
        try {
            String filterKey = "new";

            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String testTweetMessage = "Model Message";
            Date testDate = new Date(2323223232L);
            String urlString = "";
            URL testProfileUrl = null;

            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();

            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage + " NEWNEWNEW", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel1);

            TwitterTweetModel tweetModel3 = createTweetModel(testDate, testTweetMessage + " new", testProfileUrl, testScreenName, testName, null);
            expected.add(tweetModel3);

            when(mockedCache.getEntry(isA(String.class))).thenReturn(expected);
            twitterService.setCache(mockedCache);

            List<TwitterTweetModel> result = twitterService.getFiltered(filterKey).get();
            assertTrue(expected.size() > 0);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i), result.get(i));
            }

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testPostStatusSuccess() {
        try {
            String message = "Sample tweet";

            String testName = "Model Name";
            String testScreenName = "Model Screen Name";
            String testTweetMessage = message;
            Date testDate = new Date(2323223232L);
            String urlString = "";
            URL testProfileUrl = null;

            TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName, null);
            Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName, 0);

            when(twitter.updateStatus(message)).thenReturn(status);

            TwitterTweetModel result = twitterService.postStatus(message).get();
            assertEquals(expected, result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testPostStatusFailEmpty() {
        try {
            String message = "";

            Optional<TwitterTweetModel> result = twitterService.postStatus(message);
            assertEquals(Optional.empty(), result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testPostStatusFailHuge() {
        try {
            String message = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);

            Optional<TwitterTweetModel> result = twitterService.postStatus(message);
            assertEquals(Optional.empty(), result);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testPublicConstructor() {
        twitterService = new TwitterService(null);
        assertEquals(null, twitterService.getTwitter());
    }
}
