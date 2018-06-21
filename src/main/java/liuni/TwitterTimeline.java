package liuni;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Status;

import java.util.List;

public class TwitterTimeline {
    public List<Status> getTimelineJson() throws Exception {
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        return statuses;

    }
}
