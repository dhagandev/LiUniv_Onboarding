package liuni;

import liuni.models.ErrorModel;
import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockingDetails;

public class TwitterTweetModelTest {
    private TwitterTweetModel tweetModel;

    @Before
    public void setUp() {
        tweetModel = new TwitterTweetModel();
    }

    @Test
    public void testGetUser() {
        UserModel user = mock(UserModel.class);
        tweetModel.setUser(user);
        UserModel result = tweetModel.getUser();
        assertEquals(user, result);
    }

    @Test
    public void testGetCreatedAt() {
        Date date = mock(Date.class);
        tweetModel.setCreatedAt(date);
        Date result = tweetModel.getCreatedAt();
        assertEquals(date, result);
    }

    @Test
    public void testTwoUnequalModelsAll() {
        TwitterTweetModel tweetModel1 = new TwitterTweetModel();
        tweetModel1.setCreatedAt(null);
        tweetModel1.setUser(null);
        tweetModel1.setMessage(null);
        TwitterTweetModel tweetModel2 = new TwitterTweetModel();
        tweetModel2.setCreatedAt(new Date());
        tweetModel2.setUser(mock(UserModel.class));
        tweetModel2.setMessage("Not null message");

        assertNotEquals(tweetModel1, tweetModel2);
    }

    @Test
    public void testTwoUnequalModelsMessage() {
        Date d = new Date();
        UserModel userModel = mock(UserModel.class);
        TwitterTweetModel tweetModel1 = new TwitterTweetModel();
        tweetModel1.setCreatedAt(d);
        tweetModel1.setUser(userModel);
        tweetModel1.setMessage("Different message 1");
        TwitterTweetModel tweetModel2 = new TwitterTweetModel();
        tweetModel2.setCreatedAt(d);
        tweetModel2.setUser(userModel);
        tweetModel2.setMessage("Different message 2");

        assertNotEquals(tweetModel1, tweetModel2);
    }

    @Test
    public void testTwoUnequalModelsDate() {
        Date d1 = new Date();
        Date d2 = new Date(2323223232L);
        UserModel userModel = mock(UserModel.class);
        TwitterTweetModel tweetModel1 = new TwitterTweetModel();
        tweetModel1.setCreatedAt(d1);
        tweetModel1.setUser(userModel);
        tweetModel1.setMessage("Same message");
        TwitterTweetModel tweetModel2 = new TwitterTweetModel();
        tweetModel2.setCreatedAt(d2);
        tweetModel2.setUser(userModel);
        tweetModel2.setMessage("Same message");

        assertNotEquals(tweetModel1, tweetModel2);
    }

    @Test
    public void testTwoUnequalModelsUser() {
        Date d = new Date();
        UserModel userModel = mock(UserModel.class);
        TwitterTweetModel tweetModel1 = new TwitterTweetModel();
        tweetModel1.setCreatedAt(d);
        tweetModel1.setUser(null);
        tweetModel1.setMessage("Same message");
        TwitterTweetModel tweetModel2 = new TwitterTweetModel();
        tweetModel2.setCreatedAt(d);
        tweetModel2.setUser(userModel);
        tweetModel2.setMessage("Same message");

        assertNotEquals(tweetModel1, tweetModel2);
    }

    @Test
    public void testTwoEqualModels() {
        Date d = new Date();
        UserModel userModel = mock(UserModel.class);
        TwitterTweetModel tweetModel1 = new TwitterTweetModel();
        tweetModel1.setCreatedAt(d);
        tweetModel1.setUser(userModel);
        tweetModel1.setMessage("Same message");

        assertEquals(tweetModel1, tweetModel1);
    }

    @Test
    public void testTwoOppositeModels() {
        TwitterTweetModel tweetModel = new TwitterTweetModel();
        ErrorModel error = new ErrorModel();

        assertNotEquals(tweetModel, error);
    }
}
