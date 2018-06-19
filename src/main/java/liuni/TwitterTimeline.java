package liuni;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;


import java.util.List;

public class TwitterTimeline {
    public void getTimeline() {
        Twitter twitter = TwitterFactory.getSingleton();
        try {
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Fetching your home timeline...");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ": " + status.getText());
                System.out.println(" MORE INFO =======================================");
                System.out.println(status.toString());
                System.out.println("==================================================");

            }
        }
        catch (TwitterException e) {
            System.out.println("An error occurred in fetching your timeline!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public JSONObject[] getTimelineJson() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        try {
            List<Status> statuses = twitter.getHomeTimeline();

            JSONObject[] jsonObjects = new JSONObject[statuses.size()];
            for (int i = 0; i < statuses.size(); i++) {
                try {
                    String rawJsonStr = TwitterObjectFactory.getRawJSON(statuses.get(i));
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(rawJsonStr);
                    jsonObjects[i] = jsonObject;
                }
                catch (ParseException e) {
                    System.out.println("An error occurred fetching the following status: " + statuses.get(i).getId());
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            return jsonObjects;
        }
        catch (TwitterException e) {
            System.out.println("An error occurred in fetching your timeline!");
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }
}
