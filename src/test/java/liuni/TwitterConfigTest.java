package liuni;

import liuni.configs.TwitterUserConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TwitterConfigTest {
    private TwitterUserConfig config;

    @Before
    public void setUp() {
        config = new TwitterUserConfig();
    }

    @Test
    public void test_SetUserName() {
        String testKey = "Test Value";
        config.setUserName(testKey);
        assertEquals(testKey, config.getUserName());
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

}
