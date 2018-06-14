package LiUni;

import java.util.Scanner;

public class InputRequests {

    //Prompt Strings
    private static String validInput = "Input 'Y' or 'y' for yes, 'N' or 'n' for no. 'E' or 'e' will exit the program. ";
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String EXIT = "E";
    private static String unrecognizedInput = "I'm sorry, I didn't understand that. ";
    private static String postTweet = "Do you want to post a tweet? ";
    private static String getTimeline = "Do you want to see your timeline? ";

    //True = Input is valid; False = Input has some error
    private static boolean validInput(String input) {
        if (input.equals(YES) || input.equals(NO) || input.equals(EXIT)) {
            return true;
        }
        return false;
    }

    private void postingTweet(Scanner reader) {
        TwitterStatus twitterStatusHandler = new TwitterStatus();
        System.out.println("What do you want to tweet? Currently only text is supported.");
        String userInput = reader.nextLine();
        twitterStatusHandler.postStatus(userInput);
        promptTimeline(reader);
    }

    private void retrieveTimeline(Scanner reader) {
        TwitterTimeline twitterTimelineHandler = new TwitterTimeline();
        twitterTimelineHandler.getTimeline();
        promptPost(reader);
    }

    private String getUserInput(Scanner reader) {
        String userInput = reader.nextLine().toUpperCase();

        boolean validUserInput = validInput(userInput);
        while (!validUserInput) {
            System.out.println(unrecognizedInput + validInput);
            userInput = reader.nextLine().toUpperCase();
            validUserInput = validInput(userInput);
        }

        return userInput;
    }

    public void promptTimeline(Scanner reader) {
        System.out.println(getTimeline + validInput);
        String userInput = getUserInput(reader);

        if (userInput.equals(EXIT)) {
            System.out.println("Thank you! Goodbye.");
        }
        else if (userInput.equals(YES)) {
            retrieveTimeline(reader);
        }
        else {
            promptPost(reader);
        }
    }

    public void promptPost(Scanner reader) {
        System.out.println(postTweet + validInput);
        String userInput = getUserInput(reader);

        if (userInput.equals(EXIT)) {
            System.out.println("Thank you! Goodbye.");
        }
        else if (userInput.equals(YES)) {
            postingTweet(reader);
        }
        else {
            promptTimeline(reader);
        }
    }

}
