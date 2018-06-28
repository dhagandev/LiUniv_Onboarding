package liuni;

import com.codahale.metrics.health.HealthCheck;
import liuni.health.LiUniHealthCheck;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;

public class HealthCheckTest {

    @Test
    public void healthCheck_Good() {
        LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        assertEquals(HealthCheck.Result.healthy().isHealthy(), healthCheck.check().isHealthy());
    }
}
