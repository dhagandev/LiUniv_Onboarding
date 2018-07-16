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
    private List<Status> timelineFull;
    private List<TwitterTweetModel> timelineCondensed;

    public TwitterTimelineModel() throws TwitterException {
        timelineFull = TwitterService.getInstance().getTimeline();
    }

    public List<Status> getTimelineFull() {
        return timelineFull;
    }

    @JsonProperty
    public List<TwitterTweetModel> getTimelineCondensed() {
        timelineCondensed = convertTimeline();
        return timelineCondensed;
    }

    private List<TwitterTweetModel> convertTimeline() {
        List<TwitterTweetModel> list = new ArrayList<TwitterTweetModel>();
        for (Status status:timelineFull) {
            TwitterTweetModel tweet = new TwitterTweetModel();
            UserModel user = new UserModel();
            User twitterUser = status.getUser();

            user.setName(twitterUser.getScreenName());
            user.setTwitterHandle(twitterUser.getName());
            URL url = null;
            try {
                url = new URL(twitterUser.getProfileImageURL());
            }
            catch (MalformedURLException e) {
                logger.error("Improper URL:", e);
            }
            user.setProfileImageUrl(url);

            tweet.setMessage(status.getText());
            tweet.setUser(user);
            tweet.setCreatedAt(status.getCreatedAt());

            list.add(tweet);
        }
        return list;
    }
}
