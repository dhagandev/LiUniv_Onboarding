package liuni;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

public class LiUniConfiguration extends Configuration {
    @NotEmpty
    private String defaultMessage = "This is a default tweet posted from a route.";

    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();

    @JsonProperty
    public String getDefaultMessage() {
        return defaultMessage;
    }

    @JsonProperty
    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @JsonProperty("viewRendererConfiguration")
    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap.builder();
        for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration.entrySet()) {
            builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
        }
        this.viewRendererConfiguration = builder.build();
    }
}
