package liuni.health;

import com.codahale.metrics.health.HealthCheck;

public class LiUniHealthCheck extends HealthCheck {
    @Override
    protected Result check() {
        return Result.healthy();
    }
}
