package LiUni;

import java.util.Scanner;

public class LiUniDriver {
    public static void main(String args[]) {
        Scanner reader = new Scanner(System.in);
        KeyHandler keyHandler = new KeyHandler();
        keyHandler.setupKeys();
        InputRequests iReqs = new InputRequests();
        iReqs.promptPost(reader);
    }
}
