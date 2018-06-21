package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;

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

    @JsonProperty
    public JSONObject toJSON() {
        JSONObject jObj = new JSONObject();
        jObj.put("error", error);
        return jObj;
    }
}
