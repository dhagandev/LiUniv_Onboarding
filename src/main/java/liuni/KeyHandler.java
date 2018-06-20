package liuni;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class KeyHandler {
    private static final String TWITTER = "Twitter";
    private static final String HCKEY_FILE = "hardcoded_keys.xml";

    public void setupKeys() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            File hcKeys = new File(HCKEY_FILE);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(hcKeys);
            doc.getDocumentElement().normalize();

            NodeList serviceList = doc.getElementsByTagName("service");
            for (int i = 0; i < serviceList.getLength(); i++) {
                Node service = serviceList.item(i);
                Element element = (Element) service;
                String serviceName = element.getAttribute("id");

                //Ideally would use switch case based on what serviceName is
                if (serviceName.equals(TWITTER)) {
                    System.out.println("Setting up " + serviceName + " service...");
                    setupTwitter(element);
                } else {
                    System.out.println("Unsupported service presented. " + serviceName);
                }
            }

        }
        catch (Exception e) {
            System.out.println("Could not set up the keys. " + e.toString());
            e.printStackTrace();
            System.exit(-2);
        }
    }

    private static void setupTwitter(Element element) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("twitter4j.properties"));
            writer.write("debug=false\n");

            String conKey = element.getElementsByTagName("consumerKey").item(0).getTextContent();
            String conSec = element.getElementsByTagName("consumerSecret").item(0).getTextContent();
            String accToken = element.getElementsByTagName("accessToken").item(0).getTextContent();
            String accSec = element.getElementsByTagName("accessSecret").item(0).getTextContent();

            writer.write("oauth.consumerKey=" + conKey + "\n");
            writer.write("oauth.consumerSecret=" + conSec + "\n");
            writer.write("oauth.accessToken=" + accToken + "\n");
            writer.write("oauth.accessTokenSecret=" + accSec + "\n");
        }
        catch (IOException e) {
            System.out.println("Error occurred when setting up twitter4j.properties.");
            e.printStackTrace();
            System.exit(-2);
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                try {
                    Twitter twitter = TwitterFactory.getSingleton();
                    twitter.verifyCredentials();
                }
                catch (TwitterException e) {
                    System.out.println("Error occurred; improper credentials for Twitter.");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            catch (IOException e) {
                System.out.println("Error occurred when closing the Buffered writer.");
                System.out.println(e.toString());
                System.exit(-2);
            }
        }
    }
}
