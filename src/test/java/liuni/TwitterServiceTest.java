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
    private Status status;
    private String testUserName;
    private String testUserScreenName;
    private String testUserProfileURL;
    private URL testURL;
    private String testMessage;
    private Date date;

    @Before
    public void setUp() {
        twitterService = TwitterService.getInstance();
        twitter = mock(Twitter.class);
        twitterService.setTwitter(twitter);
    }

    private void setUpUserBadURL() {
        testUserName = "TestUserName";
        testUserScreenName = "TestUserScreenName";
        testUserProfileURL = "BadURL";

        status = mock(Status.class);
        User user = mock(User.class);
        when(user.getName()).thenReturn(testUserName);
        when(user.getScreenName()).thenReturn(testUserScreenName);
        when(user.getProfileImageURL()).thenReturn(testUserProfileURL);
        when(status.getUser()).thenReturn(user);
    }

    private void setUpUserGoodURL() {
        testUserName = "TestUserName";
        testUserScreenName = "TestUserScreenName";
        testUserProfileURL = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";

        testURL = null;
        try {
            testURL = new URL(testUserProfileURL);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }

        assertTrue(testURL != null);

        status = mock(Status.class);
        User user = mock(User.class);
        when(user.getName()).thenReturn(testUserName);
        when(user.getScreenName()).thenReturn(testUserScreenName);
        when(user.getProfileImageURL()).thenReturn(testUserProfileURL);
        when(status.getUser()).thenReturn(user);
    }

    @Test
    public void testGetUserBadURL() {
        setUpUserBadURL();
        UserModel resultModel = twitterService.getUser(status);
        assertEquals(testUserName, resultModel.getName());
        assertEquals(testUserScreenName, resultModel.getTwitterHandle());
        assertEquals(null, resultModel.getProfileImageUrl());
    }

    @Test
    public void testGetUserGoodURL() {
        setUpUserGoodURL();
        UserModel resultModel = twitterService.getUser(status);
        assertEquals(testUserName, resultModel.getName());
        assertEquals(testUserScreenName, resultModel.getTwitterHandle());
        assertEquals(testURL, resultModel.getProfileImageUrl());
    }

    private void setUpTweet() {
        date = new Date(2323223232L);
        testMessage = "Test Message";
        when(status.getText()).thenReturn(testMessage);
        when(status.getCreatedAt()).thenReturn(date);
    }

    @Test
    public void test_getTweet() {
        setUpUserBadURL();
        setUpTweet();

        TwitterTweetModel tweetModel = twitterService.getTweet(status);
        assertEquals(date, tweetModel.getCreatedAt());
        assertEquals(testMessage, tweetModel.getMessage());
        assertEquals(testUserName, tweetModel.getUser().getName());
        assertEquals(testUserScreenName, tweetModel.getUser().getTwitterHandle());
        assertEquals(null, tweetModel.getUser().getProfileImageUrl());
    }

    private TwitterTweetModel createTweetModel() {
        TwitterTweetModel tweetModel = new TwitterTweetModel();
        tweetModel.setCreatedAt(date);
        tweetModel.setMessage(testMessage);

        UserModel user = new UserModel();
        user.setProfileImageUrl(null);
        user.setTwitterHandle(testUserScreenName);
        user.setName(testUserName);

        tweetModel.setUser(user);

        return tweetModel;
    }

    private TwitterTweetModel getSpareModel() {
        String testName = "Spare Model Name";
        String testScreenName = "Spare Model Screen Name";
        URL testProfileUrl = null;
        String testTweetMessage = "Spare Model Message";
        Date testDate = new Date(2323223232L);

        TwitterTweetModel tweetModel = new TwitterTweetModel();
        UserModel userModel = new UserModel();
        userModel.setName(testName);
        userModel.setTwitterHandle(testScreenName);
        userModel.setProfileImageUrl(testProfileUrl);
        tweetModel.setMessage(testTweetMessage);
        tweetModel.setUser(userModel);
        tweetModel.setCreatedAt(testDate);

        return tweetModel;
    }

    private Status getSpareStatus() {
        String testName = "Spare Model Name";
        String testScreenName = "Spare Model Screen Name";
        String testTweetMessage = "Spare Model Message";
        Date testDate = new Date(2323223232L);

        Status spareStatus = mock(Status.class);
        User user = mock(User.class);
        when(user.getName()).thenReturn(testName);
        when(user.getScreenName()).thenReturn(testScreenName);
        when(user.getProfileImageURL()).thenReturn("");
        when(spareStatus.getUser()).thenReturn(user);
        when(spareStatus.getText()).thenReturn(testTweetMessage);
        when(spareStatus.getCreatedAt()).thenReturn(testDate);
        return spareStatus;
    }

    @Test
    public void testConvertTwitterResultsToTwitterTweetModelList() {
        try {
            setUpUserBadURL();
            setUpTweet();

            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
            TwitterTweetModel tweetModel1 = createTweetModel();
            expected.add(tweetModel1);
            TwitterTweetModel tweetModel2 = getSpareModel();
            expected.add(tweetModel2);

            ResponseList<Status> responseList = new ResponseListImpl<Status>();
            responseList.add(status);
            Status status2 = getSpareStatus();
            responseList.add(status2);
            when(twitter.getHomeTimeline()).thenReturn(responseList);

            List<TwitterTweetModel> result = twitterService.getTimeline();
            assertEquals(expected.get(0).getCreatedAt(), result.get(0).getCreatedAt());
            assertEquals(expected.get(0).getMessage(), result.get(0).getMessage());
            assertEquals(expected.get(0).getUser().getName(), result.get(0).getUser().getName());
            assertEquals(expected.get(0).getUser().getTwitterHandle(), result.get(0).getUser().getTwitterHandle());
            assertEquals(expected.get(0).getUser().getProfileImageUrl(), result.get(0).getUser().getProfileImageUrl());

            assertEquals(expected.get(1).getCreatedAt(), result.get(1).getCreatedAt());
            assertEquals(expected.get(1).getMessage(), result.get(1).getMessage());
            assertEquals(expected.get(1).getUser().getName(), result.get(1).getUser().getName());
            assertEquals(expected.get(1).getUser().getTwitterHandle(), result.get(1).getUser().getTwitterHandle());
            assertEquals(expected.get(1).getUser().getProfileImageUrl(), result.get(1).getUser().getProfileImageUrl());

        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

}
