package liuni;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.Status;

public class TwitterTimeline {
    private Twitter twitter;
    private TwitterConfig config;

    public TwitterTimeline(TwitterConfig config) {
        this.config = config;
        if (this.config != null) {
            twitter = this.config.createTwitterConfig();
        }
        else {
            twitter = null;
        }
    }

    public TwitterConfig getConfig() {
        return config;
    }

    public ResponseList<Status> getTimeline() throws Exception {
        ResponseList<Status> statuses = twitter.getHomeTimeline();
        return statuses;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }
}
