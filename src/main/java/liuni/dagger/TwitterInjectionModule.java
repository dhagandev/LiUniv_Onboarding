package liuni.dagger;

import dagger.Module;
import dagger.Provides;
import liuni.configs.LiUniConfig;
import liuni.configs.TwitterAccountConfig;
import liuni.configs.TwitterConfig;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Module
public class TwitterInjectionModule {
    private static Twitter twitter;
    LiUniConfig liUniConfig;
    private TwitterAccountConfig twitterAccountConfig;
    private TwitterConfig twitterConfig;
    private int defaultAccountIndex;

    public TwitterInjectionModule(LiUniConfig config) {
        liUniConfig = config;
    }

    @Provides
    Twitter provideTwitter() {
        if (liUniConfig != null) {
            setUpConfiguration(liUniConfig.getTwitter());
            return twitter;
        }
        return null;
    }

    public void setUpConfiguration(TwitterConfig config) {
        this.twitterConfig = config;

        boolean configNotNull = this.twitterConfig != null;
        if (configNotNull) {
            defaultAccountIndex = config.getDefaultAccountIndex();
            int size = config.getTwitterAccounts().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = defaultAccountIndex >= 0 && defaultAccountIndex < size;
            if (configListNotEmpty && indexInBounds) {
                setTwitterAccountConfig(this.twitterConfig.getTwitterAccounts().get(defaultAccountIndex));
            }
        }
    }

    public TwitterConfig getTwitterConfig() {
        return twitterConfig;
    }

    public void setTwitterConfig(TwitterConfig config) {
        this.twitterConfig = config;
    }

    public void setConfigIndex(int index) {
        int size = this.twitterConfig.getTwitterAccounts().size();
        boolean indexInBounds = index >= 0 && index < size;
        if (indexInBounds) {
            defaultAccountIndex = index;
            setTwitterAccountConfig(this.twitterConfig.getTwitterAccounts().get(index));
        }
    }

    public int getDefaultAccountIndex() {
        return defaultAccountIndex;
    }

    public void createTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(twitterAccountConfig.getConsumerKey());
        cb.setOAuthConsumerSecret(twitterAccountConfig.getConsumerSecret());
        cb.setOAuthAccessToken(twitterAccountConfig.getAccessToken());
        cb.setOAuthAccessTokenSecret(twitterAccountConfig.getAccessSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }

    public void setTwitterAccountConfig(TwitterAccountConfig config) {
        this.twitterAccountConfig = config;
        createTwitter();
    }

    public TwitterAccountConfig getTwitterAccountConfig() {
        return twitterAccountConfig;
    }
}
