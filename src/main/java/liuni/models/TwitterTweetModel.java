package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class TwitterTweetModel {
    private String message;
    private UserModel user;
    private Date createdAt;
    private URL link;

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
    public URL getLink() {
        return link;
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

    @JsonProperty
    public void setLink(URL link) {
        this.link = link;
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

    @Override
    public int hashCode() {
        return Objects.hash(message, user, createdAt);
    }
}
