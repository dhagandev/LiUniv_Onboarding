package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import org.json.simple.JSONObject;

public class TwitterTimelineModel {
    private long id;

    @Length(max = 3)
    private JSONObject[] timeline;

    public TwitterTimelineModel() {
        //Jackson deserialization
    }

    public TwitterTimelineModel(long id, JSONObject[] timeline) {
        this.id = id;
        this.timeline = timeline;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public JSONObject[] getTimeline() {
        return timeline;
    }

}
