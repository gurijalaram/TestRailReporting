package com.apriori.cus.utils;

import com.apriori.TestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.junit.jupiter.api.BeforeAll;

public class CUSTestUtil extends TestUtil {
    protected static UserCredentials testingUser;

    @BeforeAll
    public static void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser = UserUtil.getUser("admin"));
        RequestEntityUtil.useTokenForRequests(testingUser.getToken());
    }
}
