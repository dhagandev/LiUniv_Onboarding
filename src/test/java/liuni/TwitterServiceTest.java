//package liuni;
//
//import liuni.configs.TwitterAccountConfig;
//import liuni.configs.TwitterConfig;
//import liuni.models.TwitterTweetModel;
//import liuni.models.UserModel;
//import liuni.services.TwitterService;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import twitter4j.ResponseList;
//import twitter4j.Status;
//import twitter4j.Twitter;
//import twitter4j.User;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.isA;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class TwitterServiceTest {
//    private TwitterService twitterService;
//    private Twitter twitter;
//    private TwitterConfig mockedConfig;
//
//    @Before
//    public void setUp() {
//        twitterService = TwitterService.getInstance();
//        twitter = mock(Twitter.class);
//        mockedConfig = mock(TwitterConfig.class);
//        twitterService.setTwitter(twitter);
//    }
//
//    public TwitterTweetModel createTweetModel(Date testDate, String testMessage, URL url, String testScreenName, String testName) {
//        TwitterTweetModel tweetModel = new TwitterTweetModel();
//        UserModel user = createUserModel(url, testScreenName, testName);
//        tweetModel.setUser(user);
//        tweetModel.setCreatedAt(testDate);
//        tweetModel.setMessage(testMessage);
//
//        return tweetModel;
//    }
//
//    public UserModel createUserModel(URL url, String testScreenName, String testName) {
//        UserModel user = new UserModel();
//        user.setProfileImageUrl(url);
//        user.setTwitterHandle(testScreenName);
//        user.setName(testName);
//
//        return user;
//    }
//
//    public Status createMockedStatus(Date testDate, String testMessage, String urlString, String testScreenName, String testName) {
//        Status status = mock(Status.class);
//        User user = createdMockedUser(urlString, testScreenName, testName);
//        when(status.getUser()).thenReturn(user);
//        when(status.getText()).thenReturn(testMessage);
//        when(status.getCreatedAt()).thenReturn(testDate);
//
//        return status;
//    }
//
//    public User createdMockedUser(String urlString, String testScreenName, String testName) {
//        User mockUser = mock(User.class);
//        when(mockUser.getName()).thenReturn(testName);
//        when(mockUser.getScreenName()).thenReturn(testScreenName);
//        when(mockUser.getProfileImageURL()).thenReturn(urlString);
//
//        return mockUser;
//    }
//
//    @Test
//    public void testGetTweet() {
//        String testName = "Model Name";
//        String testScreenName = "Model Screen Name";
//        String testTweetMessage = "Model Message";
//        Date testDate = new Date(2323223232L);
//        String urlString = "";
//        URL testProfileUrl = null;
//
//        TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
//        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//
//        TwitterTweetModel result = twitterService.getTweet(status);
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testGetUserBadURL() {
//        String testName = "Model Name";
//        String testScreenName = "Model Screen Name";
//        String testTweetMessage = "Model Message";
//        Date testDate = new Date(2323223232L);
//        String urlString = "";
//        URL testProfileUrl = null;
//
//        UserModel expected = createUserModel(testProfileUrl, testScreenName, testName);
//        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//
//        UserModel result = twitterService.getUser(status);
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testGetUserGoodURL() {
//        String testName = "Model Name";
//        String testScreenName = "Model Screen Name";
//        String testTweetMessage = "Model Message";
//        Date testDate = new Date(2323223232L);
//        String urlString = "http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
//        URL testProfileUrl = null;
//        try {
//            testProfileUrl = new URL(urlString);
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//
//        UserModel expected = createUserModel(testProfileUrl, testScreenName, testName);
//        Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//
//        UserModel result = twitterService.getUser(status);
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testGetTimeline() {
//        try {
//            String testName = "Model Name";
//            String testScreenName = "Model Screen Name";
//            String testTweetMessage = "Model Message";
//            Date testDate = new Date(2323223232L);
//            String urlString = "";
//            URL testProfileUrl = null;
//
//            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
//            ResponseList<Status> responseList = new ResponseListImpl<Status>();
//
//            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
//            expected.add(tweetModel1);
//            Status status1 = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//            responseList.add(status1);
//
//            TwitterTweetModel tweetModel2 = createTweetModel(testDate, testTweetMessage + " add on", testProfileUrl, testScreenName, testName);
//            expected.add(tweetModel2);
//            Status status2 = createMockedStatus(testDate, testTweetMessage + " add on", urlString, testScreenName, testName);
//            responseList.add(status2);
//
//            when(twitter.getHomeTimeline()).thenReturn(responseList);
//
//            List<TwitterTweetModel> result = twitterService.getTimeline().get();
//            assertTrue(expected.size() > 0);
//            assertEquals(expected.size(), result.size());
//            for (int i = 0; i < expected.size(); i++) {
//                assertEquals(expected.get(i), result.get(i));
//            }
//
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//    }
//
//    @Test
//    public void testGetFiltered() {
//        try {
//            String filterKey = "new";
//
//            String testName = "Model Name";
//            String testScreenName = "Model Screen Name";
//            String testTweetMessage = "Model Message";
//            Date testDate = new Date(2323223232L);
//            String urlString = "";
//            URL testProfileUrl = null;
//
//            List<TwitterTweetModel> expected = new ArrayList<TwitterTweetModel>();
//            ResponseList<Status> responseList = new ResponseListImpl<Status>();
//
//            TwitterTweetModel tweetModel1 = createTweetModel(testDate, testTweetMessage + " NEWNEWNEW", testProfileUrl, testScreenName, testName);
//            expected.add(tweetModel1);
//            Status status1 = createMockedStatus(testDate, testTweetMessage + " NEWNEWNEW", urlString, testScreenName, testName);
//            responseList.add(status1);
//
//            Status status2 = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//            responseList.add(status2);
//
//            TwitterTweetModel tweetModel3 = createTweetModel(testDate, testTweetMessage + " new", testProfileUrl, testScreenName, testName);
//            expected.add(tweetModel3);
//            Status status3 = createMockedStatus(testDate, testTweetMessage + " new", urlString, testScreenName, testName);
//            responseList.add(status3);
//
//            when(twitter.getHomeTimeline()).thenReturn(responseList);
//
//            List<TwitterTweetModel> result = twitterService.getFiltered(filterKey).get();
//            assertTrue(expected.size() > 0);
//            assertEquals(expected.size(), result.size());
//            for (int i = 0; i < expected.size(); i++) {
//                assertEquals(expected.get(i), result.get(i));
//            }
//
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//    }
//
//    @Test
//    public void testPostStatusSuccess() {
//        try {
//            String message = "Sample tweet";
//
//            String testName = "Model Name";
//            String testScreenName = "Model Screen Name";
//            String testTweetMessage = message;
//            Date testDate = new Date(2323223232L);
//            String urlString = "";
//            URL testProfileUrl = null;
//
//            TwitterTweetModel expected = createTweetModel(testDate, testTweetMessage, testProfileUrl, testScreenName, testName);
//            Status status = createMockedStatus(testDate, testTweetMessage, urlString, testScreenName, testName);
//
//            when(twitter.updateStatus(message)).thenReturn(status);
//
//            TwitterTweetModel result = twitterService.postStatus(message).get();
//            assertEquals(expected, result);
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//    }
//
//    @Test
//    public void testPostStatusFailEmpty() {
//        try {
//            String message = "";
//
//            Optional<TwitterTweetModel> result = twitterService.postStatus(message);
//            assertEquals(Optional.empty(), result);
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//    }
//
//    @Test
//    public void testPostStatusFailHuge() {
//        try {
//            String message = StringUtils.repeat("*", TwitterService.TWITTER_CHAR_MAX + 1);
//
//            Optional<TwitterTweetModel> result = twitterService.postStatus(message);
//            assertEquals(Optional.empty(), result);
//        }
//        catch (Exception e) {
//            Assert.fail("This exception is not expected.");
//        }
//    }
//
//    @Test
//    public void testCreateTwitterAndAccountConfig() {
//        String mockedStrVal = "Mocked Test Field ";
//
//        TwitterAccountConfig mockedTwitterAccConfig = mock(TwitterAccountConfig.class);
//        when(mockedTwitterAccConfig.getConsumerKey()).thenReturn(mockedStrVal + "1");
//        when(mockedTwitterAccConfig.getConsumerSecret()).thenReturn(mockedStrVal + "2");
//        when(mockedTwitterAccConfig.getAccessToken()).thenReturn(mockedStrVal + "3");
//        when(mockedTwitterAccConfig.getAccessSecret()).thenReturn(mockedStrVal + "4");
//
//        twitterService.setTwitterAccountConfig(mockedTwitterAccConfig);
//        assertNotEquals(null, twitterService.getTwitter());
//    }
//
//    @Test
//    public void testGetSetConfig() {
//        TwitterConfig retrievedConfig = twitterService.getConfig();
//        TwitterConfig mockedTwitterConfig = mock(TwitterConfig.class);
//        twitterService.setConfig(mockedTwitterConfig);
//        assertNotEquals(retrievedConfig, mockedTwitterConfig);
//    }
//
//    @Test
//    public void testSetConfigIndex() {
//        int newIndex = 1;
//
//        assertNotEquals(newIndex, twitterService.getDefaultAccountIndex());
//
//        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
//        accConfigList.add(mock(TwitterAccountConfig.class));
//        accConfigList.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);
//
//        twitterService.setConfig(mockedConfig);
//        twitterService.setConfigIndex(newIndex);
//
//        assertEquals(newIndex, twitterService.getDefaultAccountIndex());
//    }
//
//    @Test
//    public void testSetConfigIndexOutofBoundsHigh() {
//        int newIndex = 1;
//
//        assertNotEquals(newIndex, twitterService.getDefaultAccountIndex());
//
//        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
//        accConfigList.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);
//
//        twitterService.setConfig(mockedConfig);
//        twitterService.setConfigIndex(newIndex);
//
//        assertNotEquals(newIndex, twitterService.getDefaultAccountIndex());
//    }
//
//    @Test
//    public void testSetConfigIndexOutofBoundLow() {
//        int newIndex = -1;
//
//        assertNotEquals(newIndex, twitterService.getDefaultAccountIndex());
//
//        List<TwitterAccountConfig> accConfigList = new ArrayList<TwitterAccountConfig>();
//        accConfigList.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(accConfigList);
//
//        twitterService.setConfig(mockedConfig);
//        twitterService.setConfigIndex(newIndex);
//
//        assertNotEquals(newIndex, twitterService.getDefaultAccountIndex());
//    }
//
//    @Test
//    public void testConfigNull() {
//        twitterService.setUpConfiguration(null);
//        assertEquals(null, twitterService.getConfig());
//    }
//
//    @Test
//    public void testConfigListNotEmptyIndexHigh() {
//        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
//        when(mockedConfig.getDefaultAccountIndex()).thenReturn(mockedAccConfigs.size()+1);
//
//        TwitterAccountConfig expected = twitterService.getTwitterAccountConfig();
//        twitterService.setUpConfiguration(mockedConfig);
//        TwitterAccountConfig result = twitterService.getTwitterAccountConfig();
//
//        assert(twitterService.getDefaultAccountIndex() >= mockedAccConfigs.size());
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testConfigListNotEmptyIndexLow() {
//        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
//        when(mockedConfig.getDefaultAccountIndex()).thenReturn(-1);
//
//        TwitterAccountConfig expected = twitterService.getTwitterAccountConfig();
//        twitterService.setUpConfiguration(mockedConfig);
//        TwitterAccountConfig result = twitterService.getTwitterAccountConfig();
//
//        assert(twitterService.getDefaultAccountIndex() < 0);
//        assertEquals(expected, result);
//    }
//
//    @Test
//    public void testConfigListNotEmptyIndexAppropriate() {
//        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        mockedAccConfigs.add(mock(TwitterAccountConfig.class));
//        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
//        when(mockedConfig.getDefaultAccountIndex()).thenReturn(0);
//
//        TwitterAccountConfig expected = twitterService.getTwitterAccountConfig();
//        twitterService.setUpConfiguration(mockedConfig);
//        TwitterAccountConfig result = twitterService.getTwitterAccountConfig();
//
//        assert(twitterService.getDefaultAccountIndex() >= 0);
//        assert(twitterService.getDefaultAccountIndex() < mockedAccConfigs.size());
//        assertNotEquals(expected, result);
//    }
//
//    @Test
//    public void testConfigListEmpty() {
//        List<TwitterAccountConfig> mockedAccConfigs = new ArrayList<TwitterAccountConfig>();
//        when(mockedConfig.getTwitterAccounts()).thenReturn(mockedAccConfigs);
//        when(mockedConfig.getDefaultAccountIndex()).thenReturn(0);
//
//        TwitterAccountConfig expected = twitterService.getTwitterAccountConfig();
//        twitterService.setUpConfiguration(mockedConfig);
//        TwitterAccountConfig result = twitterService.getTwitterAccountConfig();
//
//        assertEquals(expected, result);
//    }
//}
