package liuni;

import org.junit.Before;
import org.junit.Test;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TwitterConfigTest {

    private TwitterConfig config;

    @Before
    public void setUp() {
        config = new TwitterConfig();
        Map<String, String> map = new HashMap<String, String>();
        map.put("consumerKey", "key");
        map.put("consumerSecret", "key");
        map.put("accessToken", "key");
        map.put("accessSecret", "key");
        config.setTwitter(map);
    }

    @Test
    public void test_SetConKey() {
        String testKey = "Test Value";
        config.setConsumerKey(testKey);
        assertEquals(testKey, config.getConsumerKey());
    }

    @Test
    public void test_SetConSec() {
        String testKey = "Test Value";
        config.setConsumerSecret(testKey);
        assertEquals(testKey, config.getConsumerSecret());
    }

    @Test
    public void test_SetAccToken() {
        String testKey = "Test Value";
        config.setAccessToken(testKey);
        assertEquals(testKey, config.getAccessToken());
    }

    @Test
    public void test_SetAccSecret() {
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
