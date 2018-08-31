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
    private final static String TIMELINE_CACHE_KEY = "timeline";
    private final static String USER_TIMELINE_CACHE_KEY = "usertimeline";
    private final static String FILTER_CACHE_KEY = "filter_";
    private final static Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private String url = "http://twitter.com/";
    private String path = "/status/";
    private TimeCache cache;

    public Twitter twitter;


    @Inject
    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
        cache = new TimeCache(60, 20);
    }
    public void setCache(TimeCache cache) {
        this.cache = cache;
    }

    public Twitter getTwitter() {
        return twitter;
    }

    public Optional<TwitterTweetModel> postStatus(String text) throws TwitterException {
        boolean isOkToPost = textErrorCheck(text);
        if (isOkToPost) {
            return Stream.of(twitter.updateStatus(text))
                         .peek(status -> {
                             logger.info("Successfully updated status to [" + status.getText() + "].");
                             cache.clearCache();
                         })
                         .map(status -> getTweet(status))
                         .findFirst();
        }
        return Optional.empty();
    }

    public Optional<List<TwitterTweetModel>> getTimeline() throws TwitterException {
        return (cache.getEntry(TIMELINE_CACHE_KEY) != null) ? Optional.of(cache.getEntry(TIMELINE_CACHE_KEY)) : Optional.of(cache.putEntry(TIMELINE_CACHE_KEY, twitter.getHomeTimeline().stream()
                                                                                                                                                                      .map(status -> getTweet(status))
                                                                                                                                                                      .collect(Collectors.toList())));
    }

    public Optional<List<TwitterTweetModel>> getUserTimeline() throws TwitterException {
        return (cache.getEntry(USER_TIMELINE_CACHE_KEY) != null) ? Optional.of(cache.getEntry(USER_TIMELINE_CACHE_KEY)) : Optional.of(cache.putEntry(USER_TIMELINE_CACHE_KEY, twitter.getUserTimeline().stream()
                                                                                                                                                                      .map(status -> getTweet(status))
                                                                                                                                                                      .collect(Collectors.toList())));
    }

    public Optional<List<TwitterTweetModel>> getFiltered(String filterKey) throws TwitterException {
        String cacheKey = FILTER_CACHE_KEY + filterKey;
        return (cache.getEntry(cacheKey) != null) ? Optional.of(cache.getEntry(cacheKey)) : Optional.of(cache.putEntry(cacheKey, twitter.getHomeTimeline().stream()
                                                                                                                                        .filter(status -> status.getText().toLowerCase().contains(filterKey.toLowerCase()))
                                                                                                                                        .map(status -> getTweet(status))
                                                                                                                                        .collect(Collectors.toList())));
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTweetLinkStr(UserModel user, Status status) {
        return url + user.getTwitterHandle() + path + status.getId();
    }

    public TwitterTweetModel getTweet(Status status) {
        TwitterTweetModel tweet = new TwitterTweetModel();
        UserModel user = getUser(status);

        URL url = null;
        try {
            url = new URL(getTweetLinkStr(user, status));
        }
        catch (MalformedURLException e) {
            logger.error("Improper URL:", e);
        }
        tweet.setLink(url);
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
