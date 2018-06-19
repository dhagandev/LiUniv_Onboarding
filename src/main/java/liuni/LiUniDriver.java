package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class LiUniDriver extends Application<LiUniConfiguration> {
    public static void main(String args[]) {
        KeyHandler keyHandler = new KeyHandler();
        keyHandler.setupKeys();
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(LiUniConfiguration config, Environment env) {
        final LiUniResource resource = new LiUniResource();
        final LiUniHealthCheck healthCheck = new LiUniHealthCheck(config.getDefaultMessage());
        env.healthChecks().register("defaultMessage", healthCheck);
        env.jersey().register(resource);
    }
}
