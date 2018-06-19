package liuni.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitterPostApiRep {
    private long id;

    private String message;

    public TwitterPostApiRep() {
        //Jackson deserialization
    }

    public TwitterPostApiRep(long id, String message) {
        this.id = id;
        this.message = message;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getTweet() {
        return message;
    }
}
