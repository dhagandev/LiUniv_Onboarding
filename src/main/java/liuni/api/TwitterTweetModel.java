package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.json.simple.JSONObject;

public class TwitterTweetModel {
    private long id;

    @Length(max = 3)
    private String message;

    public TwitterTweetModel() {
        //Jackson deserialization
    }

    public TwitterTweetModel(long id, String message) {
        this.id = id;
        this.message = message;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

}
