package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.json.simple.JSONObject;

public class TwitterTweetModel {
    private long id;

    @Length(max = 3)
    private String message;

    private String error;

    public TwitterTweetModel() {
        //Jackson deserialization
    }

    public TwitterTweetModel(long id, String message, String error) {
        this.id = id;
        this.message = message;
        this.error = error;
    }

    public TwitterTweetModel(long id, String message) {
        this.id = id;
        this.message = message;
        this.error = null;
    }

    @JsonProperty
    public long getId() {
        return id;
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
