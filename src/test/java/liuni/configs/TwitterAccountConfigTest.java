package liuni.configs;

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
    public void testSetConsumerKey() {
        String testKey = "Test Value";
        config.setConsumerKey(testKey);
        assertEquals(testKey, config.getConsumerKey());
    }

    @Test
    public void testSetConsumerSecret() {
        String testKey = "Test Value";
        config.setConsumerSecret(testKey);
        assertEquals(testKey, config.getConsumerSecret());
    }

    @Test
    public void testSetAccessToken() {
        String testKey = "Test Value";
        config.setAccessToken(testKey);
        assertEquals(testKey, config.getAccessToken());
    }

    @Test
    public void testSetAccessSecret() {
        String testKey = "Test Value";
        config.setAccessSecret(testKey);
        assertEquals(testKey, config.getAccessSecret());
    }

}
