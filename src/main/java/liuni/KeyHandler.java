package liuni;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class KeyHandler {
    private static final String TWITTER = "Twitter";
    private static final String HCKEY_FILE = "hardcoded_keys.xml";
    private Twitter twitter;
    private DocumentBuilderFactory dbFactory;
    private BufferedWriter writer;

    private String conKey;
    private String conSec;
    private String accToken;
    private String accSec;

    public KeyHandler() {
        twitter = null;
        dbFactory = DocumentBuilderFactory.newInstance();
        writer = null;
    }

    public void setTwitter(Twitter twitter) {
        this.twitter = twitter;
    }

    public void setDbFactory(DocumentBuilderFactory dbFactory) {
        this.dbFactory = dbFactory;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public String getConKey() {
        return conKey;
    }

    public String getConSec() {
        return conSec;
    }

    public String getAccToken() {
        return accToken;
    }

    public String getAccSec() {
        return accSec;
    }

    public void setupKeys() {
        try {
            File hcKeys = new File(HCKEY_FILE);
            Document doc = dbFactory.newDocumentBuilder().parse(hcKeys);

            NodeList serviceList = doc.getElementsByTagName("service");
            for (int i = 0; i < serviceList.getLength(); i++) {
                Node service = serviceList.item(i);
                Element element = (Element) service;
                String serviceName = element.getAttribute("id");

                //Ideally would use switch case based on what serviceName is
                if (serviceName.equals(TWITTER)) {
                    System.out.println("Setting up " + serviceName + " service...");
                    getTwitterKeys(element);
                    setupTwitter();
                } else {
                    System.out.println("Unsupported service presented. " + serviceName);
                }
            }

        }
        catch (Exception e) {
            System.out.println("Could not set up the keys. " + e.toString());
            e.printStackTrace();
        }
    }

    public void getTwitterKeys(Element element) {
        conKey = element.getElementsByTagName("consumerKey").item(0).getTextContent();
        conSec = element.getElementsByTagName("consumerSecret").item(0).getTextContent();
        accToken = element.getElementsByTagName("accessToken").item(0).getTextContent();
        accSec = element.getElementsByTagName("accessSecret").item(0).getTextContent();
    }

    public void setupTwitter() {
        try {
            writer.write("debug=false\n");

            writer.write("oauth.consumerKey=" + conKey + "\n");
            writer.write("oauth.consumerSecret=" + conSec + "\n");
            writer.write("oauth.accessToken=" + accToken + "\n");
            writer.write("oauth.accessTokenSecret=" + accSec + "\n");
        }
        catch (IOException e) {
            System.out.println("Error occurred when setting up twitter4j.properties.");
            e.printStackTrace();
        }
        try {
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error occurred when closing the Buffered writer.");
            e.printStackTrace();
        }
    }

    public void twitterValidCredentials() {
        try {
            twitter.verifyCredentials();
        }
        catch (TwitterException e) {
            System.out.println("Error occurred; improper credentials for Twitter.");
            e.printStackTrace();
        }
    }
}
