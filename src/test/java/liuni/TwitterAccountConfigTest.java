package liuni;

import liuni.configs.TwitterAccountConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TwitterAccountConfigTest {
    private TwitterAccountConfig config;

    @Before
    public void setUp() {
        config = new TwitterAccountConfig();
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