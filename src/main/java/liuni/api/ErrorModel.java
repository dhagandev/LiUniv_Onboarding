package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.core.Response;

public class ErrorModel {
    private final String ERROR = "We are sorry you are experiencing trouble with our app. Contact our administration if this error continues. In the mean time, we recommend going to https://chromedino.com/";

    public ErrorModel() {
        //Jackson deserialization
    }

    @JsonProperty
    public String getError() {
        return ERROR;
    }

    public Response.Status getErrorStatus() {
        return Response.Status.INTERNAL_SERVER_ERROR;
    }

}
