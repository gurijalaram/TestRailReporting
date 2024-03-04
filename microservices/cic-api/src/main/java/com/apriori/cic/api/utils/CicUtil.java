package com.apriori.cic.api.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.testconfig.TestBaseUI;

import org.junit.jupiter.api.BeforeAll;

public class CicUtil extends TestBaseUI {

    protected static RequestEntityUtil requestEntityUtil;
    protected static UserCredentials testingUser = UserUtil.getUser();

    @BeforeAll
    public static void init() {
        requestEntityUtil = RequestEntityUtilBuilder
            .useRandomUser()
            .useTokenInRequests()
            .useApUserContextInRequests();

        testingUser = requestEntityUtil.getEmbeddedUser();
    }
}
