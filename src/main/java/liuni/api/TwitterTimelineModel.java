package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.simple.JSONObject;

public class TwitterTimelineModel {
    private JSONObject[] timeline;
    private String error;

    public TwitterTimelineModel() {
        //Jackson deserialization
    }

    public TwitterTimelineModel(JSONObject[] timeline, String error) {
        this.timeline = timeline;
        this.error = error;
    }

    public TwitterTimelineModel(JSONObject[] timeline) {
        this.timeline = timeline;
        this.error = null;
    }

    @JsonProperty
    public JSONObject[] getTimeline() {
        return timeline;
    }

    @JsonProperty
    public String getError() {
        return error;
    }

}
