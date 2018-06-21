package liuni;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class TwitterTimeline {
    public JSONObject[] getTimelineJson() throws Exception {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        List<Status> statuses = twitter.getHomeTimeline();

        JSONObject[] jsonObjects = new JSONObject[statuses.size()];
        JSONParser parser = new JSONParser();
        for (int i = 0; i < statuses.size(); i++) {
            String rawJsonStr = TwitterObjectFactory.getRawJSON(statuses.get(i));
            JSONObject jsonObject = (JSONObject) parser.parse(rawJsonStr);
            jsonObjects[i] = jsonObject;
        }
        return jsonObjects;

    }
}
