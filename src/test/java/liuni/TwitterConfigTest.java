package liuni;

import liuni.configs.TwitterConfig;
import liuni.configs.TwitterAccountConfig;
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
        twitterConfig.setDefaultAccountIndex(test);
        assertEquals(test, twitterConfig.getDefaultAccountIndex());
    }

    @Test
    public void test_SetTwitterUsers() {
        List<TwitterAccountConfig> test = new ArrayList<TwitterAccountConfig>();
        test.add(mock(TwitterAccountConfig.class));
        twitterConfig.setTwitterAccounts(test);
        assertEquals(test, twitterConfig.getTwitterAccounts());
    }
}
