package liuni;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeyHandlerTest {

    @Mock private Twitter twitter;

    @Mock private KeyHandler keyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        keyHandler = mock(KeyHandler.class);
        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);
   }

   private Document getMockedDoc(String fakeXmlFile) {
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

        String fakeXmlFile = "<?xml version='1.0'?>" +
                "<services>" +
                "<service id='Twitter'>" +
                "<consumerKey>TestValue</consumerKey>" +
                "<consumerSecret>TestValue</consumerSecret>" +
                "<accessToken>TestValue</accessToken>" +
                "<accessSecret>TestValue</accessSecret>" +
                "</service>" +
                "</services>";

        try {
            Document mockedDoc = getMockedDoc(fakeXmlFile);
            DocumentBuilderFactory dbFactory = mock(DocumentBuilderFactory.class);
            keyHandler.setDbFactory(dbFactory);
            DocumentBuilder dbuilder = mock(DocumentBuilder.class);
            when(dbFactory.newDocumentBuilder()).thenReturn(dbuilder);
            when(dbuilder.parse(isA(File.class))).thenReturn(mockedDoc);
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
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Fail_DocBuilderException() {
        DocumentBuilderFactory dbFactory = mock(DocumentBuilderFactory.class);
        keyHandler.setDbFactory(dbFactory);

        try {
            when(dbFactory.newDocumentBuilder()).thenThrow(new ParserConfigurationException("This is an exception test."));
            keyHandler.setupKeys();

            verify(mock(KeyHandler.class), never()).setupTwitter();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Fail_FileWriteException() {
        BufferedWriter writer = mock(BufferedWriter.class);
        keyHandler.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            doThrow(e).when(writer).write(isA(String.class));
            keyHandler.setupTwitter();

            verify(e).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testSetupKeys_createTwitterProperties_FailWriterClose_FileWriteException() {
        BufferedWriter writer = mock(BufferedWriter.class);
        keyHandler.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            doThrow(e).when(writer).close();
            keyHandler.setupTwitter();

            verify(e).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testSetupKeys_createTwitterProperties_NullWriter() {
        BufferedWriter writer = null;
        keyHandler.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            keyHandler.setupTwitter();

            verify(mock(BufferedWriter.class), never()).close();
            verify(e, never()).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testSetUpKeys_otherServices() {
        String fakeXmlFile = "<?xml version='1.0'?>" +
                "<services>" +
                "<service id='Other'>" +
                "<consumerKey>TestValue</consumerKey>" +
                "<consumerSecret>TestValue</consumerSecret>" +
                "<accessToken>TestValue</accessToken>" +
                "<accessSecret>TestValue</accessSecret>" +
                "</service>" +
                "</services>";

        try {
            Document mockedDoc = getMockedDoc(fakeXmlFile);
            DocumentBuilderFactory dbFactory = mock(DocumentBuilderFactory.class);
            keyHandler.setDbFactory(dbFactory);
            DocumentBuilder dbuilder = mock(DocumentBuilder.class);
            when(dbFactory.newDocumentBuilder()).thenReturn(dbuilder);
            when(dbuilder.parse(isA(File.class))).thenReturn(mockedDoc);
            keyHandler.setupKeys();

            verify(mock(KeyHandler.class), never()).setupTwitter();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testTwitterValidCredentials_Fail() {
        try {
            TwitterException e = mock(TwitterException.class);
            when(twitter.verifyCredentials()).thenThrow(e);
            keyHandler.twitterValidCredentials();
            verify(e).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void testTwitterValidCredentials_Success() {
        try {
            TwitterException e = mock(TwitterException.class);
            keyHandler.twitterValidCredentials();
            verify(e, never()).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }
}
