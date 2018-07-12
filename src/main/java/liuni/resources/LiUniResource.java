package liuni.resources;

import liuni.LiUniConfig;
import liuni.api.ErrorModel;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import liuni.services.TwitterService;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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
    private static Logger logger = LoggerFactory.getLogger(LiUniResource.class);
    private LiUniConfig config;
    private TwitterService twitterService;

    public LiUniResource() {
        config = null;
        twitterService = TwitterService.getInstance();
    }

    public LiUniResource(LiUniConfig config, int index) {
        this.config = config;
        twitterService = TwitterService.getInstance();

        boolean configNotNull = this.config != null;
        if (configNotNull) {
            int size = this.config.getTwitter().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = index >= 0 && index < size;
            if (configListNotEmpty && indexInBounds) {
                twitterService.setTwitterConfig(this.config.getTwitter().get(index));
                twitterService.createTwitter();
            }
        }
    }

    public LiUniConfig getConfig() {
        return config;
    }

    public TwitterService getTwitterService() {
        return twitterService;
    }

    public void setTwitterConfig(int index) {
        twitterService.setTwitterConfig(config.getTwitter().get(index));
    }

    public void setTwitterService(TwitterService service) {
        twitterService = service;
    }

    @Path("/timeline")
    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchTimeline() {
        ResponseBuilder responseBuilder = Response.noContent();
        responseBuilder.type(MediaType.APPLICATION_JSON);
        try {
            ResponseList<Status> timeline = twitterService.getTimeline();
            responseBuilder.status(Response.Status.OK);
            responseBuilder.entity(new TwitterTimelineModel(timeline).getTimeline());
        }
        catch (TwitterException e) {
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message) {
        ResponseBuilder responseBuilder = Response.noContent();
        responseBuilder.type(MediaType.APPLICATION_JSON);
        try {
            Status status = twitterService.postStatus(message);
            if (status != null) {
                responseBuilder.status(Response.Status.CREATED);
                responseBuilder.entity(new TwitterTweetModel(message).getMessage());
            }
            else {
                ErrorModel error = new ErrorModel();
                responseBuilder.status(error.getErrorStatus());
                responseBuilder.entity(error.getBadTweetError());
                logger.warn("An error occurred. Unable to post your tweet [" + message + "]. Sorry! This may be due to the message being too long or being empty. Produced an error with a " + error.getErrorStatus() + " code.");
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
