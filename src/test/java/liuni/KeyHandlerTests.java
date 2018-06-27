package liuni;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import twitter4j.Twitter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyHandlerTests {

    private static String twitter4jFileName;

    @Mock private Twitter twitter;

    @Mock private KeyHandler keyHandler;

    @BeforeClass
    public static void prepareTests() {
        twitter4jFileName = "twitter4j.properties";
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        keyHandler = mock(KeyHandler.class);
        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);
   }

   private Document getMockedDoc() {
        final String fakeXmlFile = "<?xml version='1.0'?>" +
                "<services>" +
                "<service id='Twitter'>" +
                "<consumerKey></consumerKey>" +
                "<consumerSecret></consumerSecret>" +
                "<accessToken></accessToken>" +
                "<accessSecret></accessSecret>" +
                "</service>" +
                "</services>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
           builder = factory.newDocumentBuilder();
           StringReader strReader = new StringReader(fakeXmlFile);
           InputSource iSource = new InputSource(strReader);
           Document doc = builder.parse(iSource);
           return doc;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
   }

    /* Test KeyHandler .setupKeys() method */
    @Test
    public void testSetupKeys_twitterPropertiesWrite_Success() {
        BufferedWriter writer = mock(BufferedWriter.class);
        keyHandler.setWriter(writer);

        try {
            Document mockedDoc = getMockedDoc();
            DocumentBuilderFactory dbFactory = mock(DocumentBuilderFactory.class);
            keyHandler.setDbFactory(dbFactory);
            when(dbFactory.newDocumentBuilder().parse("hardcoded_keys.xml")).thenReturn(mockedDoc);
            keyHandler.setupKeys();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }

        try {
            verify(writer).write("debug=false\n");

            verify(writer).write("oauth.consumerKey=" + keyHandler.getConKey() + "\n");
            verify(writer).write("oauth.consumerSecret=" + keyHandler.getConSec() + "\n");
            verify(writer).write("oauth.accessToken=" + keyHandler.getAccToken() + "\n");
            verify(writer).write("oauth.accessTokenSecret=" + keyHandler.getAccSec() + "\n");
        }
        catch (Exception e) {
            Assert.fail("Error occurred when closing the Buffered writer.");
        }
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
            when(dbFactory.newDocumentBuilder()).thenThrow(new ParserConfigurationException("This is an exception test."));
            keyHandler.setupKeys();

            assertFalse(twitterPropFile.exists());
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
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
            Assert.fail("This exception is not expected.");
        }
    }
}
