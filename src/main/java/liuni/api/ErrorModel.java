package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.ws.rs.core.Response;

public class ErrorModel {
    private String error;

    public ErrorModel() {
        //Jackson deserialization
    }

    public ErrorModel(String error) {
        this.error = error;
    }

    @JsonProperty
    public String getError() {
        return error;
    }

    public Response.Status getErrorStatus() {
        if (error.contains("Authentication credentials")) {
            return Response.Status.UNAUTHORIZED;
        }
        else if (error.contains("Status is a duplicate")) {
            return Response.Status.FORBIDDEN;
        }
        else {
            return Response.Status.BAD_REQUEST;
        }
    }

}
