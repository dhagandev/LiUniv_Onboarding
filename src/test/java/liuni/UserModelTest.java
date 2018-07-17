package liuni;

import liuni.models.ErrorModel;
import liuni.models.UserModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class UserModelTest {
    private UserModel user;

    @Before
    public void setUp() {
        user = new UserModel();
    }

    @Test
    public void testGetName() {
        String test = "name";
        user.setName(test);
        String result = user.getName();
        assertEquals(test, result);
    }

    @Test
    public void testGetHandle() {
        String test = "handle";
        user.setTwitterHandle(test);
        String result = user.getTwitterHandle();
        assertEquals(test, result);
    }

    @Test
    public void testGetURL() {
        URL test = mock(URL.class);
        user.setProfileImageUrl(test);
        URL result = user.getProfileImageUrl();
        assertEquals(test, result);
    }

    @Test
    public void testTwoOppositeModels() {
        UserModel userModel = new UserModel();
        ErrorModel error = new ErrorModel();

        assertNotEquals(userModel, error);
    }

    @Test
    public void testTwoEqualModels() {
        UserModel userModel = new UserModel();
        URL url = null;
        try {
            url = new URL("https://abs.twimg.com/sticky/default_profile_images/default_profile_bigger.png");
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
        userModel.setProfileImageUrl(url);
        userModel.setName("name");
        userModel.setTwitterHandle("handle");

        assertEquals(userModel, userModel);
    }

    @Test
    public void testTwoUnequalModelsName() {
        URL url = null;
        try {
            url = new URL("https://abs.twimg.com/sticky/default_profile_images/default_profile_bigger.png");
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
        UserModel user1 = new UserModel();
        user1.setName("name1");
        user1.setTwitterHandle("handle1");
        user1.setProfileImageUrl(url);
        UserModel user2 = new UserModel();
        user2.setName("name2");
        user2.setTwitterHandle("handle1");
        user2.setProfileImageUrl(url);

        assertNotEquals(user1, user2);
    }

    @Test
    public void testTwoUnequalModelsHandle() {
        URL url = null;
        try {
            url = new URL("https://abs.twimg.com/sticky/default_profile_images/default_profile_bigger.png");
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
        UserModel user1 = new UserModel();
        user1.setName("name1");
        user1.setTwitterHandle("handle1");
        user1.setProfileImageUrl(url);
        UserModel user2 = new UserModel();
        user2.setName("name1");
        user2.setTwitterHandle("handle2");
        user2.setProfileImageUrl(url);

        assertNotEquals(user1, user2);
    }

    @Test
    public void testTwoUnequalModelsURL() {
        URL url = null;
        try {
            url = new URL("https://abs.twimg.com/sticky/default_profile_images/default_profile_bigger.png");
        }
        catch (Exception e) {
            Assert.fail("This exception is not expected.");
        }
        UserModel user1 = new UserModel();
        user1.setName("name1");
        user1.setTwitterHandle("handle1");
        user1.setProfileImageUrl(url);
        UserModel user2 = new UserModel();
        user2.setName("name1");
        user2.setTwitterHandle("handle1");
        user2.setProfileImageUrl(mock(URL.class));

        assertNotEquals(user1, user2);
    }
}
