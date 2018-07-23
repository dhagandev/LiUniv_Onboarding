package liuni.resources;

import liuni.models.ErrorModel;
import com.codahale.metrics.annotation.Timed;
import liuni.models.TwitterTweetModel;
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

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource {
    private static Logger logger = LoggerFactory.getLogger(LiUniResource.class);
    private TwitterConfig config;
    private TwitterService twitterService;
    private int defaultAccountIndex;

    public LiUniResource(TwitterConfig config) {
        this.config = config;
        twitterService = TwitterService.getInstance();

        boolean configNotNull = this.config != null;
        if (configNotNull) {
            this.defaultAccountIndex = config.getDefaultAccountIndex();
            int size = this.config.getTwitterAccounts().size();
            boolean configListNotEmpty = size > 0;
            boolean indexInBounds = defaultAccountIndex >= 0 && defaultAccountIndex < size;
            if (configListNotEmpty && indexInBounds) {
                twitterService.setTwitterAccountConfig(this.config.getTwitterAccounts().get(defaultAccountIndex));
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

    public void setTwitterService(TwitterService service) {
        twitterService = service;
    }

    @Path("/timeline")
    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fetchTimeline() {
        try {
            return Stream.of(twitterService.getTimeline())
                  .map(list -> Response.noContent()
                                       .type(MediaType.APPLICATION_JSON)
                                       .status(Response.Status.OK)
                                       .entity(list))
                  .findFirst()
                  .get()
                  .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Stream.of(Response.noContent())
                        .map(responseBuilder -> {
                            responseBuilder.type(MediaType.APPLICATION_JSON);
                            responseBuilder.status(error.getErrorStatus());
                            responseBuilder.entity(error);
                            return responseBuilder;
                        })
                        .findFirst()
                        .get()
                        .build();
        }
    }

    @Path("/tweet/filter")
    @GET
    @Timed
    public Response filterTweets(@QueryParam("key") String filterKey) {
        try {
            return Stream.of(twitterService.getFiltered(filterKey))
                         .map(list -> Response.noContent()
                                              .type(MediaType.APPLICATION_JSON)
                                              .status(Response.Status.OK)
                                              .entity(list))
                         .findFirst()
                         .get()
                         .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Stream.of(Response.noContent())
                         .map(responseBuilder -> {
                             responseBuilder.type(MediaType.APPLICATION_JSON);
                             responseBuilder.status(error.getErrorStatus());
                             responseBuilder.entity(error);
                             return responseBuilder;
                         })
                         .findFirst()
                         .get()
                         .build();
        }
    }

    @Path("/tweet")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message) {
        try {
            return Stream.of(twitterService.postStatus(message))
                         .map(status -> {
                             if (status != null) {
                                 logger.info("Successfully posted: " + status.getMessage());
                                 return Stream.of(Response.noContent())
                                              .map(responseBuilder -> {
                                                  responseBuilder.type(MediaType.APPLICATION_JSON);
                                                  responseBuilder.status(Response.Status.CREATED);
                                                  responseBuilder.entity(status);
                                                  return responseBuilder;
                                              })
                                              .findFirst()
                                              .get();
                             }
                             else {
                                 ErrorModel error = new ErrorModel();
                                 error.setError(ErrorModel.ErrorType.BAD_TWEET);
                                 logger.warn("An error occurred. Unable to post your tweet [" + message + "]. Sorry! This may be due to the message being too long or being empty. Produced an error with a " + error.getErrorStatus() + " code.");

                                 return Stream.of(Response.noContent())
                                              .map(responseBuilder -> {
                                                  responseBuilder.type(MediaType.APPLICATION_JSON);
                                                  responseBuilder.status(error.getErrorStatus());
                                                  responseBuilder.entity(error);
                                                  return responseBuilder;
                                              })
                                              .findFirst()
                                              .get();
                             }
                         })
                         .findFirst()
                         .get()
                         .build();
        }
        catch (TwitterException e) {
            ErrorModel error = new ErrorModel();
            error.setError(ErrorModel.ErrorType.GENERAL);
            logger.error("Produced an error with a " + error.getErrorStatus() + " code.", e);

            return Stream.of(Response.noContent())
                         .map(responseBuilder -> {
                             responseBuilder.type(MediaType.APPLICATION_JSON);
                             responseBuilder.status(error.getErrorStatus());
                             responseBuilder.entity(error);
                             return responseBuilder;
                         })
                         .findFirst()
                         .get()
                         .build();
        }
    }

}
