package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.enums.RolesEnum;
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
     * Use a random user received by {@link UserUtil#getUser(com.apriori.shared.util.enums.RolesEnum)}
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public static RequestEntityUtil useRandomUser(final RolesEnum role) {
        return new RequestEntityUtil(UserUtil.getUser(role));
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
