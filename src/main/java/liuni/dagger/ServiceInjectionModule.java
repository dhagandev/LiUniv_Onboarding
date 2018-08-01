package liuni.dagger;

import dagger.Module;
import dagger.Provides;
import liuni.configs.LiUniConfig;
import liuni.services.TwitterService;

import javax.inject.Singleton;

@Module
public class ServiceInjectionModule {
    private static TwitterService twitterService;
    LiUniConfig config;

    public ServiceInjectionModule(LiUniConfig config) {
        this.config = config;
    }

    @Provides
    @Singleton
    TwitterService provideTwitterService() {
        if (twitterService == null) {
            twitterService = DaggerTwitterComponent.builder()
                                                   .twitterInjectionModule(new TwitterInjectionModule(config))
                                                   .build()
                                                   .injectTwitter();
        }
        return twitterService;
    }
}
