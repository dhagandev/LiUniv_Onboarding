package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import twitter4j.Status;

import java.util.List;

public class TwitterTimelineModel {
    private List<Status> timeline;

    public TwitterTimelineModel(List<Status> timeline) {
        this.timeline = timeline;
    }

    @JsonProperty
    public List<Status> getTimeline() {
        return timeline;
    }

}
