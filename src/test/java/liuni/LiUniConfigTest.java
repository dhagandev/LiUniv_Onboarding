package liuni;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import static org.junit.Assert.assertEquals;

public class LiUniConfigTest {

    private LiUniConfig config;

    @Before
    public void setUp() {
        config = new LiUniConfig();
    }

    @Test
    public void test_SetTwitter() {
        TwitterConfig expected = mock(TwitterConfig.class);
        config.setTwitter(expected);
        assertEquals(expected, config.getTwitter());
    }
}
