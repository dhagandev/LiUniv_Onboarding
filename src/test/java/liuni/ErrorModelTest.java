package liuni;

import liuni.models.ErrorModel;
import liuni.models.TwitterTweetModel;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertNotEquals;

public class ErrorModelTest {
    @Test
    public void testTwoUnequalModels() {
        ErrorModel error1 = new ErrorModel();
        error1.setError("BAD_TWEET");
        ErrorModel error2 = new ErrorModel();
        error2.setError("");

        assertNotEquals(error1, error2);
    }

    @Test
    public void testTwoOppositeModels() {
        ErrorModel error1 = new ErrorModel();
        error1.setError("BAD_TWEET");
        TwitterTweetModel tweetModel = new TwitterTweetModel();

        assertNotEquals(error1, tweetModel);
    }

    @Test
    public void testDifferentStatus() {
        ErrorModel error1 = new ErrorModel();
        error1.setError("BAD_TWEET");
        ErrorModel error2 = new ErrorModel();
        error2.setError("");
        error2.setRespStatus(Response.Status.CONFLICT);

        assertNotEquals(error1, error2);
    }
}
