package liuni.health;

import com.codahale.metrics.health.HealthCheck;

public class LiUniHealthCheck extends HealthCheck {
    private final String template;

    public LiUniHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() {
        final String res = String.format(template, "TEST");
        if (!res.contains("TEST")) {
            return Result.unhealthy("Template doesn't include a name.");
        }
        return Result.healthy();
    }
}
