package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class TwitterTweetModel {
    private String message;
    private UserModel user;
    private Date createdAt;

    public TwitterTweetModel() {

    }

    public TwitterTweetModel(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public UserModel getUser() {
        return user;
    }

    @JsonProperty
    public Date getCreatedAt() {
        return createdAt;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public void setUser(UserModel user) {
        this.user = user;
    }

    @JsonProperty
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
