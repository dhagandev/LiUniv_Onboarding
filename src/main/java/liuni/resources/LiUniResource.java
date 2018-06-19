package liuni.resources;

import liuni.TwitterTimeline;
import liuni.api.LiUniRepresentation;
import com.codahale.metrics.annotation.Timed;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {
    private final String template;
    private final String defaultMessage;
    private final AtomicLong counter;

    public LiUniResource(String template, String defaultMessage) {
        this.template = template;
        this.defaultMessage = defaultMessage;
        this.counter = new AtomicLong();
    }

    @Path("/timeline")
    @GET
    @Timed
    public LiUniRepresentation fetchTimeline() {
        TwitterTimeline twitterTimeline = new TwitterTimeline();
        JSONObject[] timeline = twitterTimeline.getTimelineJson();
        return new LiUniRepresentation(counter.incrementAndGet(), timeline);
    }

    @Path("/tweet")
    @POST
    public LiUniRepresentation postTweet(@QueryParam("message") Optional<String> message) {
        //Tutorial:
//        final String value = String.format(template, name.orElse(defaultName));
//        return new Saying(counter.incrementAndGet(), value);
        return null;
    }
}
