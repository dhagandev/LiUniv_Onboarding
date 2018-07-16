package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.core.Response;

public class ErrorModel {
    private final String GENERAL_ERROR = "We are sorry you are experiencing trouble with our app. Contact our administration if this error continues. In the mean time, we recommend going to https://chromedino.com/";
    private final String BAD_TWEET = "Your tweet is bad and you should feel bad. But really though, tweets need to be under 280 characters and cannot be empty.";

    public ErrorModel() {
        //Jackson deserialization
    }

    @JsonProperty
    public String getGeneralError() {
        return GENERAL_ERROR;
    }

    @JsonProperty
    public String getBadTweetError() {
        return BAD_TWEET;
    }

    public Response.Status getErrorStatus() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

}
