package liuni;

import liuni.models.UserModel;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class UserModelTest {
    private UserModel user;

    @Before
    public void setUp() {
        user = new UserModel();
    }

    @Test
    public void test_getName() {
        String test = "name";
        user.setName(test);
        String result = user.getName();
        assertEquals(test, result);
    }

    @Test
    public void test_getHandle() {
        String test = "handle";
        user.setTwitterHandle(test);
        String result = user.getTwitterHandle();
        assertEquals(test, result);
    }

    @Test
    public void test_getURL() {
        URL test = mock(URL.class);
        user.setProfileImageUrl(test);
        URL result = user.getProfileImageUrl();
        assertEquals(test, result);
    }
}
