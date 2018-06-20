package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitterTweetModel {
    private String message;
    private String error;

    public TwitterTweetModel() {
        //Jackson deserialization
    }

    public TwitterTweetModel(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public TwitterTweetModel(String message) {
        this.message = message;
        this.error = null;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getError() {
        return error;
    }

}
