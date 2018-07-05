package liuni.resources;

import liuni.LiUniConfig;
import liuni.TwitterStatus;
import liuni.TwitterTimeline;
import liuni.api.ErrorModel;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {
    private TwitterTimeline twitterTimeline;
    private TwitterStatus twitterStatus;
    private static Logger logger = LoggerFactory.getLogger(LiUniResource.class);

    public LiUniResource(LiUniConfig config) {
        if (config != null) {
            twitterTimeline = new TwitterTimeline(config.getTwitter());
            twitterStatus = new TwitterStatus(config.getTwitter());
        }
        else {
            twitterTimeline = null;
            twitterStatus = null;
        }
    }

    public TwitterStatus getTwitterStatus() {
        return twitterStatus;
    }

    public TwitterTimeline getTwitterTimeline() {
        return twitterTimeline;
    }

    public void setTwitterStatus(TwitterStatus twitterStatus) {
        this.twitterStatus = twitterStatus;
    }

    public void setTwitterTimeline(TwitterTimeline twitterTimeline) {
        this.twitterTimeline = twitterTimeline;
    }

    @Path("/timeline")
    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchTimeline() {
        ResponseBuilder responseBuilder = Response.noContent();
        responseBuilder.type(MediaType.APPLICATION_JSON);
        try {
            ResponseList<Status> timeline = twitterTimeline.getTimeline();
            responseBuilder.status(Response.Status.OK);
            responseBuilder.entity(new TwitterTimelineModel(timeline).getTimeline());
        }
        catch (Exception e) {
            ErrorModel error = new ErrorModel();
            responseBuilder.status(error.getErrorStatus());
            responseBuilder.entity(error.getGeneralError());
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);
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
            boolean successfullyPosted = twitterStatus.postStatus(message);
            if (successfullyPosted) {
                responseBuilder.status(Response.Status.CREATED);
                responseBuilder.entity(new TwitterTweetModel(message).getMessage());
            }
            else {
                ErrorModel error = new ErrorModel();
                responseBuilder.status(error.getErrorStatus());
                responseBuilder.entity(error.getBadTweetError());
                logger.error("An error occurred. Unable to post your tweet [" + message + "]. Sorry! This may be due to the message being too long or being empty. Produced an error with a " + error.getErrorStatus() + " code.");
            }
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            responseBuilder.status(error.getErrorStatus());
            responseBuilder.entity(error.getGeneralError());
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);
        }
        return responseBuilder.build();
    }

}
