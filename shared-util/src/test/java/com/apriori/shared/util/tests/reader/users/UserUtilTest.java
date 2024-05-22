package com.apriori.shared.util.tests.reader.users;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;

import org.junit.jupiter.api.Test;

public class UserUtilTest {

    @Test
    public void testGetDifferentUser() {
        System.setProperty("global_different_users", "true");
        UserCredentials userCredentials = UserUtil.getUser();
        UserCredentials userCredentialsToCompare = UserUtil.getUser();

        assertNotEquals(userCredentials, userCredentialsToCompare, "Users should be different");
    }

    @Test
    public void testGetTheSameUser() {
        System.setProperty("global_different_users", "false");
        UserCredentials userCredentials = UserUtil.getUser();
        UserCredentials userCredentialsToCompare = UserUtil.getUser();

        assertEquals(userCredentials, userCredentialsToCompare, "Users should be the same");
    }

    @Test
    public void testGetTheSameUserWithAccessLevel() {
        System.setProperty("global_different_users", "false");
        UserCredentials userCredentials = UserUtil.getUser(APRIORI_DEVELOPER);
        UserCredentials userCredentialsToCompare = UserUtil.getUser(APRIORI_DEVELOPER);

        assertEquals(userCredentials, userCredentialsToCompare, "Users should be the same");
    }

    @Test
    public void testGetDifferentUserWithAccessLevel() {
        System.setProperty("global_different_users", "true");
        UserCredentials userCredentials = UserUtil.getUser(APRIORI_DEVELOPER);
        UserCredentials userCredentialsToCompare = UserUtil.getUser(APRIORI_DEVELOPER);

        assertNotEquals(userCredentials, userCredentialsToCompare, "Users should be different");
    }
}
