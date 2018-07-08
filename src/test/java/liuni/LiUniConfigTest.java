package liuni;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;

public class LiUniConfigTest {

    private LiUniConfig config;

    @Before
    public void setUp() {
        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        List<TwitterConfig> twitterConfigList = new ArrayList<TwitterConfig>();
        twitterConfigList.add(twitterConfig);
        config = new LiUniConfig();
        config.setTwitter(twitterConfigList);
    }

    @Test
    public void test_SetTwitter() {
        TwitterConfig twitterConfig = mock(TwitterConfig.class);
        List<TwitterConfig> expected = new ArrayList<TwitterConfig>();
        expected.add(twitterConfig);
        config.setTwitter(expected);
        assertEquals(expected, config.getTwitter());
    }
}
