package liuni.dagger;

import dagger.Component;
import liuni.services.TwitterService;

@Component(modules = TwitterInjectionModule.class)
public interface TwitterComponent {
    TwitterService injectTwitter();
}
