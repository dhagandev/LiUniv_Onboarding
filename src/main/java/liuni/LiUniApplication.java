package liuni;

import liuni.configs.LiUniConfig;
import liuni.dagger.DaggerLiUniComponent;
import liuni.dagger.InjectionModule;
import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiUniApplication extends Application<LiUniConfig> {
    private static Logger logger = LoggerFactory.getLogger(LiUniApplication.class);

    public static void main(String args[]) {
        try {
            new LiUniApplication().run(args);
        }
        catch (Exception e) {
            logger.error("An error occurred.", e);
        }
    }

    @Override
    public void run(LiUniConfig config, Environment env) {
        final LiUniResource resource = createResource(config);
        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }

    public LiUniResource createResource(LiUniConfig config) {
        return DaggerLiUniComponent.builder()
                                   .injectionModule(new InjectionModule(config))
                                   .build()
                                   .injectResource();
    }
}
