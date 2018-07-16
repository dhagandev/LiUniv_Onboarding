package liuni;

import liuni.services.TwitterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterServiceTest {
    private TwitterService twitterService;

    @Before
    public void setUp() {
        twitterService = TwitterService.getInstance();
    }

    @Test
    public void test_GetTimeline_Success() {
//        Twitter mockTwitter = mock(Twitter.class);
//        ResponseList<Status> list = new ResponseListImpl<Status>();
//        try {
//            when(mockTwitter.getHomeTimeline()).thenReturn(list);
//            twitterService.setTwitter(mockTwitter);
//            ResponseList<Status> result = twitterService.getTimeline();
//            assertEquals(list, result);
//        }
//        catch (TwitterException e) {
//            Assert.fail("This exception is not expected.");
//        }
    }
}
