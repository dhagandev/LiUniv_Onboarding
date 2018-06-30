package liuni;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TwitterConfigTest {

    private TwitterConfig config;

    @Before
    public void setUp() {
        config = new TwitterConfig();
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
}
