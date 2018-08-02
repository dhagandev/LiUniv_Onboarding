package liuni.dagger;

import dagger.Component;
import liuni.resources.LiUniResource;

@Component(modules = InjectionModule.class)
public interface LiUniComponent {
    LiUniResource injectResource();
}
