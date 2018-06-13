package LiUni;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.Status;

import java.util.List;

public class TwitterTimeline {
    public void getTimeline() {
        Twitter twitter = TwitterFactory.getSingleton();
        try {
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Fetching your home timeline...");
            for (Status status : statuses) {
                System.out.println(status.toString());
                System.out.println(status.getUser().getName() + ": " + status.getText());
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred in fetching your timeline!");
            e.printStackTrace();
        }
    }
}
