package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response;
import java.util.Objects;

public class ErrorModel {
    private final static String GENERAL_ERROR = "We are sorry you are experiencing trouble with our app. Contact our administration if this error continues. In the mean time, we recommend going to https://chromedino.com/";
    private final static String BAD_TWEET_ERROR = "Your tweet is bad and you should feel bad. But really though, tweets need to be under 280 characters and cannot be empty.";

    public enum ErrorType {
        BAD_TWEET, GENERAL
    }

    private String error;
    private Response.Status respStatus;

    public ErrorModel() {
        //Jackson deserialization
    }

    @JsonProperty
    public String getError() {
        return error;
    }

    @JsonProperty
    public Response.Status getErrorStatus() {
        return respStatus;
    }

    public void setError(ErrorType fault) {
        switch(fault) {
            case BAD_TWEET:
                error = BAD_TWEET_ERROR;
                break;
            default:
                error = GENERAL_ERROR;
                break;
        }
        respStatus = Response.Status.INTERNAL_SERVER_ERROR;
    }

    public void setRespStatus(Response.Status status) {
        respStatus = status;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()) {
            ErrorModel object = (ErrorModel) obj;
            boolean isStatusSame = object.getErrorStatus().equals(this.respStatus);
            boolean isErrorMessageSame = object.getError().equals(this.error);
            return isStatusSame && isErrorMessageSame;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, respStatus);
    }
}
