package liuni;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Twitter;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyHandlerTests {

    @Mock private Twitter twitter;

    @InjectMocks KeyHandler keyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* Test KeyHandler .setupKeys() method */
    @Test
    public void testSetupKeys_HCKeysExist() {
        File HCKeysFile = new File("hardcoded_keys.xml");
        assertTrue(HCKeysFile.exists());
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Success() {
        File twitterPropFile = new File("twitter4j.properties");
        if(twitterPropFile.exists()) {
            twitterPropFile.delete();
        }
        assertFalse(twitterPropFile.exists());

        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);

        keyHandler.setupKeys();
        assertTrue(twitterPropFile.exists());
        twitterPropFile.delete();
    }
}
