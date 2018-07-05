package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiUniDriver extends Application<LiUniConfig> {
    private static Logger logger;
    public static void main(String args[]) {
        logger = LoggerFactory.getLogger(LiUniDriver.class);
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            logger.info(e.getStackTrace().toString());
        }
    }

    @Override
    public void run(LiUniConfig config, Environment env) {
        final LiUniResource resource = new LiUniResource(config);

        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }

}
