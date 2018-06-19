package liuni.resources;

import liuni.TwitterStatus;
import liuni.TwitterTimeline;
import liuni.api.TwitterTimelineApiRep;
import com.codahale.metrics.annotation.Timed;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {
    private final AtomicLong counter;

    public LiUniResource() {
        this.counter = new AtomicLong();
    }

    @Path("/timeline")
    @GET
    @Timed
    public TwitterTimelineApiRep fetchTimeline() {
        TwitterTimeline twitterTimeline = new TwitterTimeline();
        JSONObject[] timeline = twitterTimeline.getTimelineJson();
        return new TwitterTimelineApiRep(counter.incrementAndGet(), timeline);
    }

    @Path("/tweet")
    @POST
    public void postTweetPOST(@RequestParam(value="messageVal", required=false) String message) {
        TwitterStatus twitterStatus = new TwitterStatus();
        twitterStatus.postStatus(message);
    }

}
