package reader.users;

import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.junit.Assert;
import org.junit.Test;

public class UserUtilTest {

    @Test
    public void testGetDifferentUser() {
        System.setProperty("global_different_users", "true");
        UserCredentials userCredentials = UserUtil.getUser();
        UserCredentials userCredentialsToCompare = UserUtil.getUser();

        Assert.assertNotEquals("Users should be different", userCredentials, userCredentialsToCompare);
    }

    @Test
    public void testGetTheSameUser() {
        System.setProperty("global_different_users", "false");
        UserCredentials userCredentials = UserUtil.getUser();
        UserCredentials userCredentialsToCompare = UserUtil.getUser();

        Assert.assertEquals("Users should be the same", userCredentials, userCredentialsToCompare);
    }

    @Test
    public void testGetTheSameUserWithAccessLevel() {
        System.setProperty("global_different_users", "false");
        UserCredentials userCredentials = UserUtil.getUser("admin");
        UserCredentials userCredentialsToCompare = UserUtil.getUser("admin");

        Assert.assertEquals("Users should be the same", userCredentials, userCredentialsToCompare);
    }

    @Test
    public void testGetDifferentUserWithAccessLevel() {
        System.setProperty("global_different_users", "true");
        UserCredentials userCredentials = UserUtil.getUser("admin");
        UserCredentials userCredentialsToCompare = UserUtil.getUser("admin");

        Assert.assertNotEquals("Users should be different", userCredentials, userCredentialsToCompare);
    }
}
