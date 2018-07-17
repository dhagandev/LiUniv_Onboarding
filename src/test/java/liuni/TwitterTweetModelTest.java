package liuni;

import liuni.models.TwitterTweetModel;
import liuni.models.UserModel;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

public class TwitterTweetModelTest {
    private TwitterTweetModel tweetModel;

    @Before
    public void setUp() {
        tweetModel = new TwitterTweetModel();
    }

    @Test
    public void test_getUser() {
        UserModel user = mock(UserModel.class);
        tweetModel.setUser(user);
        UserModel result = tweetModel.getUser();
        assertEquals(user, result);
    }

    @Test
    public void test_getCreatedAt() {
        Date date = mock(Date.class);
        tweetModel.setCreatedAt(date);
        Date result = tweetModel.getCreatedAt();
        assertEquals(date, result);
    }
}
