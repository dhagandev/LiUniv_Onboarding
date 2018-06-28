package liuni.health;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LiUniHealthCheckTest {

    @Test
    public void healthCheck_Good() {
        LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        assertEquals(HealthCheck.Result.healthy().isHealthy(), healthCheck.check().isHealthy());
    }
}
