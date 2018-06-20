package liuni.resources;

import liuni.TwitterStatus;
import liuni.TwitterTimeline;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {

    public LiUniResource() {}

    @Path("/timeline")
    @GET
    @Timed
    public TwitterTimelineModel fetchTimeline() {
        try {
            TwitterTimeline twitterTimeline = new TwitterTimeline();
            JSONObject[] timeline = twitterTimeline.getTimelineJson();
            return new TwitterTimelineModel(timeline);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new TwitterTimelineModel(null, e.toString());
        }
    }

    @Path("/tweet")
    @POST
    public TwitterTweetModel postTweetPOST(@RequestParam(value="messageVal", required=false) String message) {
        try {
            TwitterStatus twitterStatus = new TwitterStatus();
            twitterStatus.postStatus(message);
            return new TwitterTweetModel(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new TwitterTweetModel(null, e.toString());
        }
    }

}
