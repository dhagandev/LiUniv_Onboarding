package liuni;

import liuni.configs.LiUniConfig;
import liuni.dagger.DaggerLiUniComponent;
import liuni.dagger.InjectionModule;
import liuni.health.LiUniHealthCheck;
import liuni.resources.LiUniResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class LiUniApplication extends Application<LiUniConfig> {
    private static Logger logger = LoggerFactory.getLogger(LiUniApplication.class);

    public static void main(String args[]) {
        try {
            new LiUniApplication().run(args);
        }
        catch (Exception e) {
            logger.error("An error occurred.", e);
        }
    }

    @Override
    public void run(LiUniConfig config, Environment env) {
        final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        final LiUniResource resource = createResource(config);
        final LiUniHealthCheck healthCheck = new LiUniHealthCheck();
        env.healthChecks().register("TwitterHealth", healthCheck);
        env.jersey().register(resource);
    }

    public LiUniResource createResource(LiUniConfig config) {
        return DaggerLiUniComponent.builder()
                                   .injectionModule(new InjectionModule(config))
                                   .build()
                                   .injectResource();
    }
}
