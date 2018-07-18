package liuni;

import liuni.configs.LiUniConfig;
import liuni.configs.TwitterConfig;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;

public class LiUniConfigTest {

    private LiUniConfig config;

    @Before
    public void setUp() {
        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        config = new LiUniConfig();
        config.setTwitter(twitterConfig);
    }

    @Test
    public void testSetTwitter() {
        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        config.setTwitter(twitterConfig);
        assertEquals(twitterConfig, config.getTwitter());
    }
}
