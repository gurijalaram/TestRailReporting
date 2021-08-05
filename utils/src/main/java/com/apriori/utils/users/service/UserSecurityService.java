package com.apriori.utils.users.service;

import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Contain logic to work with users by access level.
 *
 * @author vzarovnyi
 */
public class UserSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

    private static Map<String, Queue<UserCredentials>> usersByAccessLevel = InitUsersData.initUsersWithAccessLevels();
    private static UserCredentials globalUser;

    /**
     * Return single user
     * if different.users is false
     * else each time return unique user
     *
     * @param accessLevel - user's access level
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser(String accessLevel) {
        return PropertiesContext.getStr("global.different_users").equals("true") ? getSecurityUser(accessLevel) : getGlobalUser();
    }

    private static synchronized UserCredentials getSecurityUser(String security) {
        Queue<UserCredentials> users = usersByAccessLevel.get(security);

        UserCredentials userCredentials = users.poll();
        users.add(userCredentials);

        return userCredentials;
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getSecurityUser(CommonConstants.DEFAULT_ACCESS_LEVEL);
        return globalUser;
    }
}
