package com.apriori.cusapi.utils;

import com.apriori.TestUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;

import org.junit.BeforeClass;

public class CUSTestUtil extends TestUtil {
    protected static UserCredentials testingUser;

    @BeforeClass
    public static void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser = UserUtil.getUser("admin"));
        RequestEntityUtil.useTokenForRequests(testingUser.getToken());
    }
}
