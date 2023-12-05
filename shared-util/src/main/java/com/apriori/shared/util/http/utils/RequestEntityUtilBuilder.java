package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;

public class RequestEntityUtilBuilder {

    /**
     * Use a random user received by {@link UserUtil#getUser()}
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public static RequestEntityUtil useRandomUser() {
        return new RequestEntityUtil(UserUtil.getUser());
    }

    /**
     * Use a random user received by {@link UserUtil#getUser(String)}
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public static RequestEntityUtil useRandomUser(final String accessLevel) {
        return new RequestEntityUtil(UserUtil.getUser(accessLevel));
    }

    /**
     * Use a custom user
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public static RequestEntityUtil useCustomUser(final UserCredentials userCredentials) {
        return new RequestEntityUtil(userCredentials);
    }
}
