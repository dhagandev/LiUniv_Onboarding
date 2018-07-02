package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LiUniDriver extends Application<TwitterConfig> {
    public static void main(String args[]) {
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(TwitterConfig config, Environment env) {
        try {
            config.setWriter(new BufferedWriter(new FileWriter("twitter4j.properties")));
            config.createTwitter4JProp();
            final LiUniResource resource = new LiUniResource();
            final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
            env.healthChecks().register("TwitterHealth", healthCheck);
            env.jersey().register(resource);
        }
        catch (IOException e) {
            System.out.println("This exception is not expected.");
        }
    }
}
