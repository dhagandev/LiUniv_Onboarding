package liuni;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.Status;

public class TwitterTimeline {
    private Twitter twitter;

    public TwitterTimeline(TwitterConfig config) {
        if (config != null) {
            twitter = config.createTwitterConfig();
        }
        else {
            twitter = null;
        }
    }

    public ResponseList<Status> getTimeline() throws Exception {
        ResponseList<Status> statuses = twitter.getHomeTimeline();
        return statuses;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}
