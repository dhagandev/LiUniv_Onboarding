package liuni;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Status;

import java.util.List;

public class TwitterTimeline {
    private static Twitter twitter;

    public TwitterTimeline() {
        twitter = TwitterFactory.getSingleton();
    }

    public ResponseList<Status> getTimeline() throws Exception {
        ResponseList<Status> statuses = twitter.getHomeTimeline();
        return statuses;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}
