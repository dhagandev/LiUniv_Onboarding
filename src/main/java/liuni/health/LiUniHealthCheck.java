package liuni.health;

import com.codahale.metrics.health.HealthCheck;

public class LiUniHealthCheck extends HealthCheck {
    private final String message;

    public LiUniHealthCheck(String message) {
        this.message = message;
    }

    @Override
    protected Result check() {
        final String res = String.format(message, "This is a default tweet message.");
        if (!res.contains("This is a default tweet message.")) {
            return Result.unhealthy("Default message is not empty.");
        }
        return Result.healthy();
    }
}
