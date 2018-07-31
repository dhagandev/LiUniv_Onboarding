package liuni.dagger;

import dagger.Component;
import liuni.resources.LiUniResource;

import javax.inject.Singleton;

@Component(modules = ServiceInjectionModule.class)
@Singleton
public interface ResourceComponent {
    LiUniResource injectResource();
}
