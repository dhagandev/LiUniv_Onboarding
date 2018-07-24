package liuni;

import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import liuni.services.TwitterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterServiceTest {
    private TwitterService twitterService;
    private Twitter twitter;

    @Before
    public void setUp() {
        twitterService = TwitterService.getInstance();
        twitter = mock(Twitter.class);
        twitterService.setTwitter(twitter);
    }

    @Test
    public void testGetUserBadURL() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        URL testProfileUrl = null;
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);

        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);

        UserModel resultModel = twitterService.getUser(status);
        assertEquals(expected.getUser(), resultModel);
    }

    @Test
    public void testGetUserGoodURL() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png\"";
        URL testProfileUrl = null;
        try {
            testProfileUrl = new URL(urlString);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);

        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
        UserModel resultModel = twitterService.getUser(status);

        assertEquals(expected.getUser(), resultModel);
    }

    @Test
    public void test_getTweet() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        URL testProfileUrl = null;
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);

        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);

        TwitterTweetModel tweetModel = twitterService.getTweet(status);
        assertEquals(expected, tweetModel);
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

    @Test
    public void testConvertTwitterResultsToTwitterTweetModelList() {
        String testName = "Model Name";
        String testScreenName = "Model Screen Name";
        String urlString = "";
        URL testProfileUrl = null;
        String testTweetMessage = "Model Message";
        Date testDate = new Date(2323223232L);
        try {
            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            ResponseList<Status> responseList = new ResponseListImpl<Status>();
            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
            expected.add(tweetModel1);
            Status status1 = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
            responseList.add(status1);
            TwitterTweetModel tweetModel2 = createTweetModel(testDate, testTweetMessage + " add on", testProfileUrl, testScreenName, testName);
            expected.add(tweetModel2);
            Status status2 = createMockedStatus(testDate, testTweetMessage + " add on", urlString, testScreenName, testName);
            responseList.add(status2);
            when(twitter.getHomeTimeline()).thenReturn(responseList);

            List<TwitterTweetModel> result = twitterService.getTimeline();
            assertTrue(expected.size() > 0);
            for (int i = 0; i < expected.size(); i++) {
                assertEquals(expected.get(i).getCreatedAt(), result.get(i).getCreatedAt());
                assertEquals(expected.get(i).getMessage(), result.get(i).getMessage());
                assertEquals(expected.get(i).getUser().getName(), result.get(i).getUser().getName());
                assertEquals(expected.get(i).getUser().getTwitterHandle(), result.get(i).getUser().getTwitterHandle());
                assertEquals(expected.get(i).getUser().getProfileImageUrl(), result.get(i).getUser().getProfileImageUrl());
            }

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

}
