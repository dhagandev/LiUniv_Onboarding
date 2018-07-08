package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiUniDriver extends Application<LiUniConfig> {
    private static Logger logger = LoggerFactory.getLogger(LiUniDriver.class);
    public static void main(String args[]) {
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            logger.error("An error occurred.", e);
        }
    }

    @Override
    public void run(LiUniConfig config, Environment env) {
        final LiUniResource resource = new LiUniResource(config, 0);

        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }

}
