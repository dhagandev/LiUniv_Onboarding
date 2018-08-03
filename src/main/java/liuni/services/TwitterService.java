package liuni.services;

import liuni.TimeCache;
import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TwitterService {
    public final static int TWITTER_CHAR_MAX = 280;
    private final static Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private TimeCache filterCache;
    private TimeCache timelineCache;

    public Twitter twitter;


    @Inject
    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
        timelineCache = new TimeCache(1, 60);
        filterCache = new TimeCache(3, 30);
    }

    public void setTimelineCache(TimeCache cache) {
        timelineCache = cache;
    }

    public void setFilterCache(TimeCache cache) {
        filterCache = cache;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public Optional<TwitterTweetModel> postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            Optional<TwitterTweetModel> tweet = Stream.of(twitter.updateStatus(text))
                                                      .peek(status -> logger.info("Successfully updated status to [" + status.getText() + "]."))
                                                      .map(status -> getTweet(status))
                                                      .findFirst();
            timelineCache.clearCache();
            filterCache.clearCache();
            return tweet;
        }
        return Optional.empty();
    }

    public Optional<List<TwitterTweetModel>> getTimeline() throws TwitterException {
        String timelineKey = "timeline";
        List<TwitterTweetModel> timelineList = timelineCache.getEntry(timelineKey);
        if (timelineList == null) {
            timelineList = twitter.getHomeTimeline().stream()
                                  .map(status -> getTweet(status))
                                  .collect(Collectors.toList());
            timelineCache.putEntry(timelineKey, timelineList);
        }
        return Optional.of(timelineList);
    }

    public Optional<List<TwitterTweetModel>> getFiltered(String filterKey) throws TwitterException {
        List<TwitterTweetModel> filteredList = filterCache.getEntry(filterKey);
        if (filteredList == null) {
            filteredList = twitter.getHomeTimeline().stream()
                                  .filter(status -> status.getText().toLowerCase().contains(filterKey.toLowerCase()))
                                  .map(status -> getTweet(status))
                                  .collect(Collectors.toList());
            filterCache.putEntry(filterKey, filteredList);
        }
        return Optional.of(filteredList);
    }

    public TwitterTweetModel getTweet(Status status) {
        TwitterTweetModel tweet = new TwitterTweetModel();
        UserModel user = getUser(status);

        tweet.setMessage(status.getText());
        tweet.setUser(user);
        tweet.setCreatedAt(status.getCreatedAt());

        return tweet;
    }

    public UserModel getUser(Status status) {
        UserModel user = new UserModel();
        User twitterUser = status.getUser();

        user.setName(twitterUser.getName());
        user.setTwitterHandle(twitterUser.getScreenName());
        URL url = null;
        try {
            url = new URL(twitterUser.getProfileImageURL());
        }
        catch (MalformedURLException e) {
            logger.error("Improper URL:", e);
        }
        user.setProfileImageUrl(url);
        return user;
    }

    // True = No errors; False = Error occurred
    private boolean textErrorCheck(String text) {
        int statusLength = text.length();
        if (statusLength > TWITTER_CHAR_MAX || text.equals("")) {
            return false;
        }

        return true;
    }
}
