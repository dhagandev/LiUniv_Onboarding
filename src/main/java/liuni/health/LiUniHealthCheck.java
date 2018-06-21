package liuni.health;

import com.codahale.metrics.health.HealthCheck;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class LiUniHealthCheck extends HealthCheck {

    public LiUniHealthCheck() { }

    @Override
    protected Result check() {
        //If there needs to be more than verify credentials, expand this to run
        //until either all results show healthy or return the first unhealthy result
        return verifyCredentials();
    }

    private Result verifyCredentials() {
        try {
            Twitter twitter = TwitterFactory.getSingleton();
            twitter.verifyCredentials();
            return Result.healthy();
        }
        catch (TwitterException e) {
            return Result.unhealthy("Error occurred; invalid credentials for Twitter.");
        }
    }
}
