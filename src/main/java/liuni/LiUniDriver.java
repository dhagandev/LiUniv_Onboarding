package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.slf4j.LoggerFactory;

public class LiUniDriver extends Application<LiUniConfig> {
    public static void main(String args[]) {
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            LoggerFactory.getLogger(LiUniDriver.class).error("An error occurred.\n", e);
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
