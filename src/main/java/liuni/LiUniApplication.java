package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiUniApplication extends Application<LiUniConfig> {
    private static Logger logger = LoggerFactory.getLogger(LiUniApplication.class);
    private static int index;

    public static void main(String args[]) {
        try {
            index = Integer.valueOf(args[2]);
            new LiUniApplication().run(args[0], args[1]);
        }
        catch (Exception e) {
            logger.error("An error occurred.", e);
        }
    }

    @Override
    public void run(LiUniConfig config, Environment env) {
        final LiUniResource resource = new LiUniResource(config, index);

        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }
}
