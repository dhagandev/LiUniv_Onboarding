package liuni.dagger;

import dagger.Component;
import twitter4j.Twitter;

@Component(modules = TwitterInjectionModule.class)
public interface TwitterComponent {
    Twitter injectTwitter();
}
