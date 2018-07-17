package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

public class TwitterTweetModel {
    private String message;
    private UserModel user;
    private Date createdAt;

    public TwitterTweetModel() {

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

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()) {
            TwitterTweetModel object = (TwitterTweetModel) obj;
            boolean isCreatedAtSame = object.getCreatedAt().equals(this.createdAt);
            boolean isMessageSame = object.getMessage().equals(this.message);
            boolean isUserSame = object.getUser().equals(this.user);
            return isCreatedAtSame && isMessageSame && isUserSame;
        }
        return false;
    }
}
