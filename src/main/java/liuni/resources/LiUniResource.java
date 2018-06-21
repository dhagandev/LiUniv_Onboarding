package liuni.resources;

import liuni.TwitterStatus;
import liuni.TwitterTimeline;
import liuni.api.ErrorModel;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import org.json.simple.JSONObject;

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
    public JSONObject fetchTimeline() {
        try {
            TwitterTimeline twitterTimeline = new TwitterTimeline();
            JSONObject[] timeline = twitterTimeline.getTimelineJson();
            return new TwitterTimelineModel(timeline).toJSON();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ErrorModel(e.toString()).toJSON();
        }
    }

    @Path("/tweet")
    @POST
    public JSONObject postTweet(String message) {
        try {
            TwitterStatus twitterStatus = new TwitterStatus();
            twitterStatus.postStatus(message);
            return new TwitterTweetModel(message).toJSON();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ErrorModel(e.toString()).toJSON();
        }
    }

}
