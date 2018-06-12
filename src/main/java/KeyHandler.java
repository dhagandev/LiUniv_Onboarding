import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class KeyHandler {
    public void setupKeys() {
        try {
            File hcKeys = new File("hardcoded_keys.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(hcKeys);
            doc.getDocumentElement().normalize();

            NodeList serviceList = doc.getElementsByTagName("service");
            for (int i = 0; i <= serviceList.getLength(); i++) {
                Node service = serviceList.item(i);
                Element element = (Element) service;
                String name = element.getAttribute("name");

                if (name.equals("Twitter")) {
                    setupTwitter(element);
                }
                else {
                    System.out.println("Unsupported service presented. " + name);
                }

                //For some reason it is not compatible with a switch case statement?
//                switch (name) {
//                    case "Twitter":
//                        setupTwitter(element);
//                        break;
//                    default:
//                        System.out.println("Unsupported service presented. " + name);
//                        break;
//                }
            }

        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void setupTwitter(Element element) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("twitter4j.properties"));
            writer.write("debug=true");
        }
        catch (IOException e) {
            System.out.println("Error occurred when setting up twitter4j.properties.");
            System.out.println(e.toString());
        }
    }
}
