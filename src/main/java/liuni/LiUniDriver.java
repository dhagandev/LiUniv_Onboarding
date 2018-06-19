package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LiUniDriver extends Application<LiUniConfiguration> {
    public static void main(String args[]) {
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Following code was the main method before trying to make the app REST-ful.
//        Scanner reader = new Scanner(System.in);
//        KeyHandler keyHandler = new KeyHandler();
//        keyHandler.setupKeys();
//        InputRequests iReqs = new InputRequests();
//        iReqs.promptPost(reader);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<LiUniConfiguration> bootstrap) {
        //Nothing yet.
    }

    @Override
    public void run(LiUniConfiguration config, Environment env) {
        final LiUniResource resource = new LiUniResource(
                config.getTemplate(),
                config.getDefaultName()
        );
        final LiUniHealthCheck healthCheck = new LiUniHealthCheck(config.getTemplate());
        env.healthChecks().register("template", healthCheck);
        env.jersey().register(resource);
    }
}
