package liuni;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

public class TwitterTimelineTests {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterTimeline twitterTimeline;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
    }

    @After
    public void tearDown() {
        reset(twitter);
        reset(status);
    }

    /* Test TwitterTimeline .getTimeline() method */
    @Test
    public void testTwitterTimeline_Empty() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testResultList = twitterTimeline.getTimeline();

            assertEquals(testResultList.size() == 0, true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testTwitterTimeline_Update() {
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testResultList = twitterTimeline.getTimeline();
            assertEquals(testResultList.size() == 0, true);

            respList.add(status);
            respList.size();

            testResultList = twitterTimeline.getTimeline();
            assertEquals(testResultList.equals(respList), true);
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

}
