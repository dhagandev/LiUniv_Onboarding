package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitterTweetModel {
    private String message;

    public TwitterTweetModel() {
        //Jackson deserialization
    }
    public TwitterTweetModel(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

}
