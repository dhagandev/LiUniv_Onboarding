package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;

public class TwitterTimelineModel {
    private JSONObject[] timeline;

    public TwitterTimelineModel() {
        //Jackson deserialization
    }

    public TwitterTimelineModel(JSONObject[] timeline) {
        this.timeline = timeline;
    }

    @JsonProperty
    public JSONObject[] getTimeline() {
        return timeline;
    }

    @JsonProperty
    public JSONObject toJSON() {
        JSONObject jObj = new JSONObject();
        jObj.put("timeline", timeline);
        return jObj;
    }

}
