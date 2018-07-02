package liuni;

import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.BufferedWriter;
import java.io.IOException;

public class TwitterConfig extends Configuration {
    BufferedWriter writer;

    @NotEmpty
    private String consumerKey;

    @NotEmpty
    private String consumerSecret;

    @NotEmpty
    private String accessToken;

    @NotEmpty
    private String accessSecret;

    public TwitterConfig() {
        writer = null;
    }

    @JsonProperty
    public String getConsumerKey() {
        return consumerKey;
    }

    @JsonProperty
    public String getConsumerSecret() {
        return consumerSecret;
    }

    @JsonProperty
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty
    public String getAccessSecret() {
        return accessSecret;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    @JsonProperty
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @JsonProperty
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @JsonProperty
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty
    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public void createTwitter4JProp() {
        try {
            if (writer != null) {
                writer.write("debug=false\n");

                writer.write("oauth.consumerKey=" + consumerKey + "\n");
                writer.write("oauth.consumerSecret=" + consumerSecret + "\n");
                writer.write("oauth.accessToken=" + accessToken + "\n");
                writer.write("oauth.accessTokenSecret=" + accessSecret + "\n");
            }
        }
        catch (IOException e) {
            System.out.println("Error occurred when setting up twitter4j.properties.");
            e.printStackTrace();
        }

        closeWriter();
    }

    public void closeWriter() {
        try {
            if(writer != null) {
                writer.close();
            }
        }
        catch (IOException e) {
            System.out.println("Error occurred when when closing the Buffered writer.");
            e.printStackTrace();
        }
    }
}
