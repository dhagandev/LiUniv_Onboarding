package liuni.health;

import com.codahale.metrics.health.HealthCheck;

public class LiUniHealthCheck extends HealthCheck {
    @Override
    public Result check() {
        return Result.healthy();
    }
}
