package LiUni;

import java.util.Scanner;

//This will later be converted to a test class with proper test cases.
//Currently set up for on-the-spot input, will be converted to definite Strings.

/*

Test tweets:

Should pass:
    Test tweet! Posting this from a Java program.


Should fail:
    This is a test tweet that is too long for twitter! Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus urna eros, blandit convallis luctus ut, bibendum vel felis. Suspendisse vestibulum non nisi ac molestie. Phasellus a metus interdum, vestibulum nunc nec, egestas velit. Phasellus eu dignissim turpis, id congue mauris. Aenean mi lacus, posuere maximus rutrum quis, dictum ac nulla. In vulputate non risus et consequat. Integer eget scelerisque magna. Nullam quis facilisis justo. Integer rutrum neque eu vulputate hendrerit. Nunc tristique orci id ligula tristique, non tincidunt dolor placerat. In in tristique risus. Donec euismod pretium euismod. Ut feugiat viverra vulputate. Ut fermentum hendrerit ante ac convallis. Etiam congue nisi porttitor iaculis lobortis. Duis rutrum lectus quis iaculis fermentum. Duis turpis tortor, lacinia sed ligula eget, bibendum vestibulum metus. Proin vestibulum nibh ante, et pulvinar nunc tempus eu. In viverra elit quis ex facilisis euismod. Nunc id vulputate leo.

 */

public class InputRequests {

    //Prompt Strings
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String EXIT = "E";
    private static String validInput = "Input 'Y' or 'y' for yes, 'N' or 'n' for no. ";
    private static String unrecognizedInput = "I'm sorry, I didn't understand that. ";
    private static String postTweet = "Do you want to post a tweet? ";
    private static String getTimeline = "Do you want to see your timeline? ";

    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);
        KeyHandler keyHandler = new KeyHandler();
        keyHandler.setupKeys();

        promptPost(reader);
    }

    //True = Input is valid; False = Input has some error
    private static boolean validInput(String input) {
        if (input.equals(YES) || input.equals(NO)) {
            return true;
        }
        return false;
    }

    private static void postingTweet(Scanner reader) {
        TwitterStatus twitterStatusHandler = new TwitterStatus();
        System.out.println("What do you want to tweet? Currently only text is supported.");
        String userInput = reader.nextLine();
        twitterStatusHandler.postStatus(userInput);
        promptTimeline(reader);
    }

    private static void retrieveTimeline(Scanner reader) {
        TwitterTimeline twitterTimelineHandler = new TwitterTimeline();
        twitterTimelineHandler.getTimeline();
        promptPost(reader);
    }

    private static String getUserInput(Scanner reader) {
        String userInput = reader.nextLine().toUpperCase();

        boolean validUserInput = validInput(userInput);
        while (!validUserInput) {
            System.out.println(unrecognizedInput + validInput);
            userInput = reader.nextLine().toUpperCase();
            validUserInput = validInput(userInput);
        }

        return userInput;
    }

    private static void promptTimeline(Scanner reader) {
        System.out.println(getTimeline + validInput);
        String userInput = getUserInput(reader);

        if (userInput.equals(YES)) {
            retrieveTimeline(reader);
        }
        else {
            promptPost(reader);
        }
    }

    private static void promptPost(Scanner reader) {
        System.out.println(postTweet + validInput);
        String userInput = getUserInput(reader);

        if (userInput.equals(YES)) {
            postingTweet(reader);
        }
        else {
            promptTimeline(reader);
        }
    }

}
