package liuni.health;

import com.codahale.metrics.health.HealthCheck;

public class LiUniHealthCheck extends HealthCheck {
    private final String message;

    public LiUniHealthCheck(String message) {
        this.message = message;
    }

    @Override
    protected Result check() {
        final String res = String.format(message, "TEST");
        if (!res.contains("TEST")) {
            return Result.unhealthy("Default message is not empty.");
        }
        return Result.healthy();
    }
}
