package com.apriori.utils.users;

import com.apriori.utils.users.service.UserCommonService;
import com.apriori.utils.users.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get user functionality.
 * Has reference to {@link com.apriori.utils.constants.Constants#environment}.properties file
 * reference properties:
 * - different.users
 * - if true: will return each time new user
 * - if false: will return each time single user
 * - users.csv.file: the name of csv file with users list from resources/{@link com.apriori.utils.constants.Constants#environment} folder
 * (if users are absent, return default user with:
 * - username:{@link com.apriori.utils.constants.Constants#defaultUserName}
 * - password:{@link com.apriori.utils.constants.Constants#defaultPassword}
 * )
 * <p>
 * Users list is global for two Collections:
 * - security users collection
 * - common users collection
 * <p>
 * Each collection has a copy of this list and after getting the user, this user will be pushed to the end of queue
 * Example:
 * security collection - user1, user2, user3
 * common collection - user1, user2, user3
 *
 * after getting the security user
 *
 * security collection - user2, user3, user1
 * common collection - user1, user2, user3
 *
 * @author vzarovnyi
 */
public class UserUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

    /**
     * Return common user
     *
     * @return
     */
    public static UserCredentials getUser() {
        UserCredentials user = UserCommonService.getUser();
        logInfo(user);

        return user;
    }

    /**
     * Return user by access level
     *
     * @param accessLevel
     * @return
     */
    public static UserCredentials getUser(String accessLevel) {
        UserCredentials user = UserSecurityService.getUser(accessLevel);
        logInfo(user);

        return user;
    }

    private static void logInfo(UserCredentials user) {
        logger.info(String.format("Received for tests USERNAME:%s PASSWORD:%s ACCESS_LEVEL:%s", user.getUsername(), user.getPassword(), user.getAccessLevel()));
    }

}
