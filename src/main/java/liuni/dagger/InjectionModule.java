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
public class InjectionModule {
    private Twitter twitter;
    private LiUniConfig liUniConfig;
    private TwitterAccountConfig twitterAccountConfig;
    private TwitterConfig twitterConfig;
    private int defaultAccountIndex;

    public InjectionModule(LiUniConfig config) {
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

    private void setUpConfiguration(TwitterConfig config) {
        this.twitterConfig = config;

        boolean configNotNull = this.twitterConfig != null;
        if (configNotNull) {
            defaultAccountIndex = config.getDefaultAccountIndex();
            int size = config.getTwitterAccounts().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = defaultAccountIndex >= 0 && defaultAccountIndex < size;
            if (configListNotEmpty && indexInBounds) {
                this.twitterAccountConfig = this.twitterConfig.getTwitterAccounts().get(defaultAccountIndex);
                createTwitter();
            }
        }
    }

    private void createTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(false);
        cb.setOAuthConsumerKey(twitterAccountConfig.getConsumerKey());
        cb.setOAuthConsumerSecret(twitterAccountConfig.getConsumerSecret());
        cb.setOAuthAccessToken(twitterAccountConfig.getAccessToken());
        cb.setOAuthAccessTokenSecret(twitterAccountConfig.getAccessSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }
}
