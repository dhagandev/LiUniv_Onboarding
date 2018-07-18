package liuni.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class UserModel {
    private String twitterHandle;
    private String name;
    private URL profileImageUrl;

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getTwitterHandle() {
        return twitterHandle;
    }

    @JsonProperty
    public URL getProfileImageUrl() {
        return profileImageUrl;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    @JsonProperty
    public void setProfileImageUrl(URL profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()) {
            UserModel object = (UserModel) obj;
            boolean isNameSame = object.getName().equals(this.name);
            boolean isTwitterHandleSame = object.getTwitterHandle().equals(this.twitterHandle);
            boolean isProfileImageUrlSame;
            if (object.getProfileImageUrl() != null && this.profileImageUrl != null) {
                isProfileImageUrlSame = object.getProfileImageUrl().toString().equals(this.profileImageUrl.toString());
            }
            else {
                isProfileImageUrlSame = object.getProfileImageUrl() == null && this.profileImageUrl == null;
            }
            return isNameSame && isTwitterHandleSame && isProfileImageUrlSame;
        }
        return false;
    }
}
