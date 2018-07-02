package liuni;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertEquals;

public class TwitterConfigTest {

    @InjectMocks TwitterConfig config;

    @Before
    public void setUp() {
        config = new TwitterConfig();
    }

    @Test
    public void test_SetConKey() {
        String testKey = "Test Value";
        config.setConsumerKey(testKey);
        assertEquals(testKey, config.getConsumerKey());
    }

    @Test
    public void test_SetConSec() {
        String testKey = "Test Value";
        config.setConsumerSecret(testKey);
        assertEquals(testKey, config.getConsumerSecret());
    }

    @Test
    public void test_SetAccToken() {
        String testKey = "Test Value";
        config.setAccessToken(testKey);
        assertEquals(testKey, config.getAccessToken());
    }

    @Test
    public void test_SetAccSecret() {
        String testKey = "Test Value";
        config.setAccessSecret(testKey);
        assertEquals(testKey, config.getAccessSecret());
    }

    @Test
    public void test_closeWriter_Null() {
        BufferedWriter writer = null;
        config.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            config.closeWriter();

            verify(e, never()).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void test_closeWriter_Exception() {
        BufferedWriter writer = mock(BufferedWriter.class);
        config.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            doThrow(e).when(writer).close();
            config.closeWriter();

            verify(e).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void test_closeWriter_Success() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("twitter4j.properties"));
            config.setWriter(writer);
            assertTrue(writer != null);
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }

        try {
            IOException e = mock(IOException.class);
            config.closeWriter();

            verify(e, never()).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void test_createTwitter4JProp_Success() {
        try {
            BufferedWriter writer = mock(BufferedWriter.class);
            config.setWriter(writer);
            assertTrue(writer != null);

            config.createTwitter4JProp();

            verify(writer).write("debug=false\n");

            verify(writer).write("oauth.consumerKey=" + config.getConsumerKey() + "\n");
            verify(writer).write("oauth.consumerSecret=" + config.getConsumerSecret() + "\n");
            verify(writer).write("oauth.accessToken=" + config.getAccessToken() + "\n");
            verify(writer).write("oauth.accessTokenSecret=" + config.getAccessSecret() + "\n");
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void test_createTwitter4JProp_Null() {
        try {
            BufferedWriter writer = null;
            config.setWriter(writer);

            config.createTwitter4JProp();

            IOException e = mock(IOException.class);
            config.createTwitter4JProp();

            verify(e, never()).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }

    @Test
    public void test_createTwitter4JProp_Exception() {
        BufferedWriter writer = mock(BufferedWriter.class);
        config.setWriter(writer);

        try {
            IOException e = mock(IOException.class);
            doThrow(e).when(writer).write(isA(String.class));
            config.createTwitter4JProp();

            verify(e).printStackTrace();
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
    }
}
