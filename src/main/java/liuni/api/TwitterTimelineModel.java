package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import org.json.simple.JSONObject;

public class TwitterTimelineModel {
    private long id;

    @Length(max = 3)
    private JSONObject[] timeline;

    private String error;

    public TwitterTimelineModel() {
        //Jackson deserialization
    }

    public TwitterTimelineModel(long id, JSONObject[] timeline, String error) {
        this.id = id;
        this.timeline = timeline;
        this.error = error;
    }

    public TwitterTimelineModel(long id, JSONObject[] timeline) {
        this.id = id;
        this.timeline = timeline;
        this.error = null;
    }

    @JsonProperty
    public long getId() {
        return id;
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
