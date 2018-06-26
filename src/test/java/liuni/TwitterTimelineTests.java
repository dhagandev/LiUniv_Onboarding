package liuni;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

public class TwitterTimelineTests {
    @Mock private Twitter twitter;
    @Mock private Status status;

    @InjectMocks TwitterTimeline twitterTimeline;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* Test TwitterTimeline .getTimeline() method */
    @Test
    public void testTwitterTimeline_Empty() {
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testList = twitterTimeline.getTimeline();
            testResult = testList.size() == 0;
        }
        catch (Exception e) {
            testResult = false;
        }
        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

    @Test
    public void testTwitterTimeline_Update() {
        twitterTimeline = new TwitterTimeline();
        twitterTimeline.setTwitter(twitter);
        ResponseList<Status> respList = new ResponseListImpl<Status>();
        boolean testResult;
        try {
            doReturn(respList).when(twitter).getHomeTimeline();

            List<Status> testResultList = twitterTimeline.getTimeline();
            testResult = testResultList.size() == 0;
            assertTrue(testResult);

            respList.add(status);
            respList.size();

            testResultList = twitterTimeline.getTimeline();
            testResult = testResultList.size() == 1;
        }
        catch (Exception e) {
            testResult = false;
        }
        assertTrue(testResult);
        reset(twitter);
        reset(status);
    }

}
