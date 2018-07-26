package liuni;

import liuni.configs.LiUniConfig;
import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import liuni.services.TwitterService;
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
        final LiUniResource resource = new LiUniResource(TwitterService.getInstance());
        resource.setUpConfiguration(config.getTwitter());

        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }
}
