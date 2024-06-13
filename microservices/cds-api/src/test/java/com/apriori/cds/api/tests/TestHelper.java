package com.apriori.cds.api.tests;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;

public class TestHelper {
    public static RequestEntityUtil init() {
        return RequestEntityUtilBuilder
            .useRandomUser(APRIORI_DEVELOPER)
            .useApUserContextInRequests();
    }
}
