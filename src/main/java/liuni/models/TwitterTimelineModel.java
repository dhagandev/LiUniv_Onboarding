package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import liuni.services.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TwitterTimelineModel {
    private static Logger logger = LoggerFactory.getLogger(TwitterTimelineModel.class);

    public TwitterTimelineModel() {

    }

    public List<TwitterTweetModel> getTimeline() throws TwitterException {
        return TwitterService.getInstance().getTimeline();
    }



}
