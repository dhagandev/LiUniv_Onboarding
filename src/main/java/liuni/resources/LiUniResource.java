package liuni.resources;

import liuni.configs.LiUniConfig;
import liuni.api.ErrorModel;
import liuni.api.TwitterTimelineModel;
import com.codahale.metrics.annotation.Timed;
import liuni.api.TwitterTweetModel;
import liuni.configs.TwitterConfig;
import liuni.configs.TwitterUserConfig;
import liuni.services.TwitterService;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
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
    private TwitterConfig config;
    private TwitterService twitterService;
    private int defaultUserIndex;

    public LiUniResource(TwitterConfig config) {
        this.config = config;
        twitterService = TwitterService.getInstance();

        boolean configNotNull = this.config != null;
        if (configNotNull) {
            this.defaultUserIndex = config.getDefaultUser();
            int size = this.config.getTwitterUsers().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = defaultUserIndex >= 0 && defaultUserIndex < size;
            if (configListNotEmpty && indexInBounds) {
                twitterService.setTwitterUserConfig(this.config.getTwitterUsers().get(defaultUserIndex));
                twitterService.createTwitter();
            }
        }
    }

    public TwitterConfig getConfig() {
        return config;
    }

    public void setConfig(TwitterConfig config) {
        this.config = config;
    }

    public TwitterService getTwitterService() {
        return twitterService;
    }

    public void setConfigIndex(int index) {
        twitterService.setTwitterUserConfig(this.config.getTwitterUsers().get(index));
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
