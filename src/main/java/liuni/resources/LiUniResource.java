package liuni.resources;

import liuni.TwitterStatus;
import liuni.TwitterTimeline;
import liuni.api.ErrorModel;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import twitter4j.Status;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.List;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {

    public LiUniResource() {}

    @Path("/timeline")
    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchTimeline() {
        ResponseBuilder responseBuilder = Response.noContent();
        responseBuilder.type(MediaType.APPLICATION_JSON);
        try {
            TwitterTimeline twitterTimeline = new TwitterTimeline();
            List<Status> timeline = twitterTimeline.getTimelineJson();
            responseBuilder.status(Response.Status.OK);
            responseBuilder.entity(new TwitterTimelineModel(timeline).getTimeline());
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorModel error = new ErrorModel(e.getMessage());
            responseBuilder.status(error.getErrorStatus());
            responseBuilder.entity(error.getError());
        }
        return responseBuilder.build();
    }

    @Path("/tweet")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTweet(String message) {
        ResponseBuilder responseBuilder = Response.noContent();
        responseBuilder.type(MediaType.APPLICATION_JSON);
        try {
            TwitterStatus twitterStatus = new TwitterStatus();
            twitterStatus.postStatus(message);
            responseBuilder.status(Response.Status.CREATED);
            responseBuilder.entity(new TwitterTweetModel(message).getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorModel error = new ErrorModel(e.getMessage());
            responseBuilder.status(error.getErrorStatus());
            responseBuilder.entity(error.getError());
        }
        return responseBuilder.build();
    }

}
