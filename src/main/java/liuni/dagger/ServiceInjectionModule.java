package liuni.dagger;

import dagger.Module;
import dagger.Provides;
import liuni.services.TwitterService;

import javax.inject.Singleton;

@Module
public class ServiceInjectionModule {
    private static TwitterService twitterService;

    public ServiceInjectionModule() {

    }

    @Provides
    @Singleton
    TwitterService provideTwitterService() {
        if (twitterService != null) {
            return twitterService;
        }
        twitterService = TwitterService.getInstance();
        return twitterService;
    }
}
