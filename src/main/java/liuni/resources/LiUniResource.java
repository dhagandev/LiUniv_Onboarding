package liuni.resources;

import liuni.models.ErrorModel;
import com.codahale.metrics.annotation.Timed;
import liuni.services.TwitterService;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class LiUniResource implements ContainerResponseFilter {
    private static Logger logger = LoggerFactory.getLogger(LiUniResource.class);
    public TwitterService twitterService;

    @Inject
    public LiUniResource(TwitterService twitterService) {
        this.twitterService = twitterService;
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

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Headers",
                                  "origin, content-type, accept, authorization");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods",
                                  "GET, POST");
    }
}
