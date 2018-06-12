import java.util.Scanner;

//This will later be converted to a test class with proper test cases.
//Currently set up for on-the-spot input, will be converted to definite Strings.
public class InputRequests {

    //Prompt Strings
    private static String validInput = "Input 'Y' or 'y' for yes, 'N' or 'n' for no. ";
    private static String unrecognizedInput = "I'm sorry, I didn't understand that. ";
    private static String postTweet = "Do you want to post a tweet? ";
    private static String getTimeline = "Do you want to see your timeline? ";

    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);

        promptPost(reader);
    }

    //True = Input is valid; False = Input has some error
    private static boolean validInput(String input) {
        if (input.equals("Y") || input.equals("N")) {
            return true;
        }
        return false;
    }

    private static void postingTweet(Scanner reader) {
        System.out.println("Sorry! This hasn't been implemented yet. ");
        promptTimeline(reader);
    }

    private static void retrieveTimeline(Scanner reader) {
        System.out.println("Sorry! This hasn't been implemented yet. ");
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
        if (userInput.equals("Y")) {
            retrieveTimeline(reader);
        }
        else {
            promptPost(reader);
        }
    }

    private static void promptPost(Scanner reader) {
        System.out.println(postTweet + validInput);
        String userInput = getUserInput(reader);

        if (userInput.equals("Y")) {
            postingTweet(reader);
        }
        else {
            promptTimeline(reader);
        }
    }

}
