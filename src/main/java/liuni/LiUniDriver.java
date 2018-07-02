package liuni;

import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import twitter4j.Twitter;

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
        Twitter twitter = config.createTwitterConfig();

        TwitterStatus twitterStatus = new TwitterStatus();
        TwitterTimeline twitterTimeline = new TwitterTimeline();
        twitterStatus.setTwitter(twitter);
        twitterTimeline.setTwitter(twitter);

        final LiUniResource resource = new LiUniResource();
        resource.setTwitterStatus(twitterStatus);
        resource.setTwitterTimeline(twitterTimeline);

        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }
}
