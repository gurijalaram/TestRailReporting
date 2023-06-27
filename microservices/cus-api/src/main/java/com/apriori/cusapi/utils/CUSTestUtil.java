package com.apriori.cusapi.utils;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import org.junit.BeforeClass;


public class CUSTestUtil extends TestUtil {
    protected static UserCredentials testingUser;

    @BeforeClass
    public static void init() {
        RequestEntityUtil.useApUserContextForRequests(testingUser = UserUtil.getUser("admin"));
        RequestEntityUtil.useTokenForRequests(testingUser.getToken());
    }
}
