package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LiUniDriver extends Application<LiUniConfig> {
    public static void main(String args[]) {
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
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
