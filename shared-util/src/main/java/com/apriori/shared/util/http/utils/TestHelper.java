package com.apriori.shared.util.http.utils;

import static com.apriori.shared.util.enums.RolesEnum.APRIORI_DEVELOPER;

import com.apriori.shared.util.file.user.UserCredentials;

public class TestHelper {
    /**
     * Initializes a user
     *
     * @return RequestEntityUtil object
     */
    public static RequestEntityUtil initUser() {
        return RequestEntityUtilBuilder
            .useRandomUser(APRIORI_DEVELOPER)
            .useApUserContextInRequests();
    }

    /**
     * Initializes a custom user
     *
     * @return RequestEntityUtil object
     */
    public static RequestEntityUtil initCustomUser(UserCredentials userCredentials) {
        return RequestEntityUtilBuilder
            .useCustomUser(userCredentials)
            .useApUserContextInRequests();
    }
}
