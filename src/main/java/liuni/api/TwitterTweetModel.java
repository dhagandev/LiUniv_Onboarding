package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;

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

    @JsonProperty
    public JSONObject toJSON() {
        JSONObject jObj = new JSONObject();
        jObj.put("message", message);
        return jObj;
    }

}
