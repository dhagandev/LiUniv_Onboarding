package liuni;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;
import twitter4j.Twitter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyHandlerTests {

    private String twitter4jFileName;

    @Mock private Twitter twitter;

    @InjectMocks KeyHandler keyHandler;

    @Before
    public void setUp() {
        twitter4jFileName = "twitter4j.properties";

        MockitoAnnotations.initMocks(this);
        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);

        File HCKeysFile = new File("hardcoded_keys.xml");
        if (!HCKeysFile.exists()) {
            createHCKeysFile();
        }
    }

    private void createHCKeysFile() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("hardcoded_keys.xml"));
            writer.write("<?xml version=\"1.0\"?>\n");
            writer.write("<services>\n");
            writer.write("\t<service id=\"Twitter\">\n");

            String conKey = "TestConsumerKey";
            String conSec = "TestConsumerSecret";
            String accToken = "TestAccessToken";
            String accSec = "TestAccessSecret";

            writer.write("\t\t<consumerKey>" + conKey + "</consumerKey>\n");
            writer.write("\t\t<consumerSecret>" + conSec + "</consumerSecret>\n");
            writer.write("\t\t<accessToken>" + accToken + "</accessToken>\n");
            writer.write("\t\t<accessSecret>" + accSec + "</accessSecret>\n");

            writer.write("\t</service>\n");
            writer.write("</services>\n");
        }
        catch (IOException e) {
            System.out.println("Error occurred when setting up hardcoded_keys.xml for testing.");
            Assert.fail();
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            }
            catch (IOException e) {
                System.out.println("Error occurred when closing the Buffered writer.");
                Assert.fail();
            }
        }
    }

    /* Test KeyHandler .setupKeys() method */
    @Test
    public void testSetupKeys_createTwitterProperties_Success() {
        File twitterPropFile = new File(twitter4jFileName);
        if(twitterPropFile.exists()) {
            twitterPropFile.delete();
        }
        assertFalse(twitterPropFile.exists());
        try {
            keyHandler.setWriter(new BufferedWriter(new FileWriter(twitter4jFileName)));
        }
        catch (IOException e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }

        keyHandler.setupKeys();
        assertTrue(twitterPropFile.exists());
        twitterPropFile.delete();
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Fail_DocBuilderException() {
        DocumentBuilderFactory dbFactory = mock(DocumentBuilderFactory.class);
        keyHandler.setDbFactory(dbFactory);

        File twitterPropFile = new File(twitter4jFileName);
        if(twitterPropFile.exists()) {
            twitterPropFile.delete();
        }
        assertFalse(twitterPropFile.exists());

        try {
            when(dbFactory.newDocumentBuilder()).thenThrow(new ParserConfigurationException(""));
            keyHandler.setupKeys();

            assertFalse(twitterPropFile.exists());
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Fail_FileWriteException() {
        BufferedWriter writer = mock(BufferedWriter.class);
        keyHandler.setWriter(writer);

        File twitterPropFile = new File(twitter4jFileName);
        if(twitterPropFile.exists()) {
            twitterPropFile.delete();
        }
        assertFalse(twitterPropFile.exists());

        try {
            doThrow(new IOException("")).when(writer).write("");
            keyHandler.setupKeys();

            assertFalse(twitterPropFile.exists());
        }
        catch (Exception e) {
            System.out.println("This exception is not expected.");
            Assert.fail();
        }
    }
}
