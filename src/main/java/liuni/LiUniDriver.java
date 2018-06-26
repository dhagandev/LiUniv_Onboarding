package liuni;

import io.dropwizard.Configuration;
import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import twitter4j.TwitterFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LiUniDriver extends Application<Configuration> {
    public static void main(String args[]) {
        setupTwitter();
        try {
            new LiUniDriver().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run(Configuration config, Environment env) {
        final LiUniResource resource = new LiUniResource();
        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }

    private static void setupTwitter() {
        KeyHandler keyHandler = new KeyHandler();
        try {
            keyHandler.setWriter(new BufferedWriter(new FileWriter("twitter4j.properties")));
        }
        catch (IOException e) {
            System.out.println("This exception is not expected.");
        }
        keyHandler.setupKeys();
        keyHandler.setTwitter(TwitterFactory.getSingleton());
        keyHandler.twitterValidCredentials();
    }
}
