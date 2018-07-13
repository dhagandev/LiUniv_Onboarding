package liuni;

import liuni.configs.TwitterConfig;
import liuni.configs.TwitterUserConfig;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TwitterConfigTest {
    TwitterConfig twitterConfig;

    @Before
    public void setUp() {
        twitterConfig = new TwitterConfig();
    }

    @Test
    public void test_SetDefaultUser() {
        int test = 3;
        twitterConfig.setDefaultUser(test);
        assertEquals(test, twitterConfig.getDefaultUser());
    }

    @Test
    public void test_SetTwitterUsers() {
        List<TwitterUserConfig> test = new ArrayList<TwitterUserConfig>();
        test.add(mock(TwitterUserConfig.class));
        twitterConfig.setTwitterUsers(test);
        assertEquals(test, twitterConfig.getTwitterUsers());
    }
}
