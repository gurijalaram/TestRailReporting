package com.apriori.cis.api.util;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.TestUtil;

import org.junit.jupiter.api.BeforeAll;

public abstract class CISTestUtil extends TestUtil {

    protected static UserCredentials testingUser;
    protected static RequestEntityUtil requestEntityUtil;

    @BeforeAll
    public static void init() {
        requestEntityUtil = RequestEntityUtilBuilder.useRandomUser(APRIORI_DEVELOPER)
            .useApUserContextInRequests()
            .useTokenInRequests();

        testingUser = requestEntityUtil
            .getEmbeddedUser();
    }
}
