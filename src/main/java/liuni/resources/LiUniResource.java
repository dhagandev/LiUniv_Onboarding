package liuni.resources;

import liuni.models.ErrorModel;
import com.codahale.metrics.annotation.Timed;
import liuni.configs.TwitterConfig;
import liuni.services.TwitterService;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {
    private static Logger logger = LoggerFactory.getLogger(LiUniResource.class);
    private TwitterConfig config;
    private TwitterService twitterService;
    private int defaultAccountIndex;

    public LiUniResource(TwitterConfig config) {
        this.config = config;

        boolean configNotNull = this.config != null;
        if (configNotNull) {
            defaultAccountIndex = config.getDefaultAccountIndex();
            int size = config.getTwitterAccounts().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = defaultAccountIndex >= 0 && defaultAccountIndex < size;
            if (configListNotEmpty && indexInBounds) {
                twitterService = TwitterService.getInstance();
                twitterService.setTwitterAccountConfig(this.config.getTwitterAccounts().get(defaultAccountIndex));
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

    public void setTwitterService(TwitterService service) {
        this.twitterService = service;
    }

    public void setConfigIndex(int index) {
        int size = this.config.getTwitterAccounts().size();
        boolean indexInBounds = index >= 0 && index < size;
        if (indexInBounds) {
            defaultAccountIndex = index;
            twitterService.setTwitterAccountConfig(this.config.getTwitterAccounts().get(index));
        }
    }

    public int getDefaultAccountIndex() {
        return defaultAccountIndex;
    }

    @Path("/timeline")
    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchTimeline() {
        try {
            return twitterService.getTimeline()
                          .map(list -> Response.noContent()
                                             .type(MediaType.APPLICATION_JSON)
                                             .status(Response.Status.OK)
                                             .entity(list))
                          .get()
                          .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Response.noContent()
                        .type(MediaType.APPLICATION_JSON)
                        .status(error.getErrorStatus())
                        .entity(error)
                        .build();
        }
    }

    @Path("/tweet/filter")
    @GET
    @Timed
    public Response filterTweets(@QueryParam("filterMessage") String filterKey) {
        try {
            return twitterService.getFiltered(filterKey)
                          .map(list -> Response.noContent()
                                              .type(MediaType.APPLICATION_JSON)
                                              .status(Response.Status.OK)
                                              .entity(list))
                          .get()
                          .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Response.noContent()
                           .type(MediaType.APPLICATION_JSON)
                           .status(error.getErrorStatus())
                           .entity(error)
                           .build();
        }
    }

    @Path("/tweet")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message) {
        try {
            return twitterService.postStatus(message)
                                 .map(status -> {
                                     logger.info("Successfully posted: " + status.getMessage());
                                     return Response.noContent()
                                                    .type(MediaType.APPLICATION_JSON)
                                                    .status(Response.Status.CREATED)
                                                    .entity(status);
                                 })
                                 .orElseGet(() -> {
                                     ErrorModel error = new ErrorModel();
                                     error.setError(ErrorModel.ErrorType.BAD_TWEET);
                                     logger.warn("An error occurred. Unable to post your tweet [" + message + "]. Sorry! This may be due to the message being too long or being empty. Produced an error with a " + error.getErrorStatus() + " code.");

                                     return Response.noContent()
                                                    .type(MediaType.APPLICATION_JSON)
                                                    .status(error.getErrorStatus())
                                                    .entity(error);
                                 })
                                 .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Response.noContent()
                           .type(MediaType.APPLICATION_JSON)
                           .status(error.getErrorStatus())
                           .entity(error)
                           .build();
        }
    }

}
