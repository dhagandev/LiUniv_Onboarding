package liuni;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import twitter4j.Twitter;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class KeyHandlerTests {

    @Mock private Twitter twitter;

    @InjectMocks KeyHandler keyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        keyHandler = new KeyHandler();
        keyHandler.setTwitter(twitter);
    }

    /* Test KeyHandler .setupKeys() method */
    @Test
    public void testSetupKeys_HCKeysExist() {
        File HCKeysFile = new File("hardcoded_keys.xml");
        assertEquals(HCKeysFile.exists(), true);
    }

    @Test
    public void testSetupKeys_createTwitterProperties_Success() {
        File twitterPropFile = new File("twitter4j.properties");
        if(twitterPropFile.exists()) {
            twitterPropFile.delete();
        }
        assertEquals(twitterPropFile.exists(), false);

        keyHandler.setupKeys();
        assertEquals(twitterPropFile.exists(), true);
        twitterPropFile.delete();
    }
}
