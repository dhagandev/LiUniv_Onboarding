package liuni;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TwitterConfigTest {

    @InjectMocks TwitterConfig config;

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

    @Test
    public void test_CreateTwitterConfig() {
        Twitter twitter = TwitterFactory.getSingleton();
        Twitter result = config.createTwitterConfig();
        assertEquals(twitter.getClass(), result.getClass());
    }
}
