package liuni;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import static org.junit.Assert.assertEquals;

public class TwitterConfigTest {
    private TwitterConfig config;

    @Before
    public void setUp() {
        config = new TwitterConfig();
    }

    @Test
    public void test_SetConsumerKey() {
        String testKey = "Test Value";
        config.setConsumerKey(testKey);
        assertEquals(testKey, config.getConsumerKey());
    }

    @Test
    public void test_SetConsumerSecret() {
        String testKey = "Test Value";
        config.setConsumerSecret(testKey);
        assertEquals(testKey, config.getConsumerSecret());
    }

    @Test
    public void test_SetAccessToken() {
        String testKey = "Test Value";
        config.setAccessToken(testKey);
        assertEquals(testKey, config.getAccessToken());
    }

    @Test
    public void test_SetAccessSecret() {
        String testKey = "Test Value";
        config.setAccessSecret(testKey);
        assertEquals(testKey, config.getAccessSecret());
    }

    @Test
    public void test_CreateTwitterConfig() {
        Twitter twitter = TwitterFactory.getSingleton();
        Twitter result = config.createTwitterConfig();
        assertEquals(twitter.getClass(), result.getClass());
    }
}
