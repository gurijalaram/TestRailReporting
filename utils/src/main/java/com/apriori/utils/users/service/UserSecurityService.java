package com.apriori.utils.users.service;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import org.apache.commons.collections4.MultiValuedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Contain logic to work with users by access level.
 *
 * @author vzarovnyi
 */
public class UserSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

    private static MultiValuedMap<String, UserCredentials> usersByAccessLevel = InitUsersData.initUsersWithAccessLevels();
    private static UserCredentials globalUser;


    /**
     * Return single user
     * if in {@link com.apriori.utils.constants.Constants#environment}.properties file different.users is false
     * else each time return unique user
     *
     * @param accessLevel - user's access level
     * @exception NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser(String accessLevel) {
        return Constants.useDifferentUsers ? getSecurityUser(accessLevel) : getGlobalUser();
    }

    private static synchronized UserCredentials getSecurityUser(String security) {
        try {
            Iterator<UserCredentials> securityUsers = usersByAccessLevel.get(security).iterator();

            UserCredentials user = securityUsers.next();
            securityUsers.remove();
            return user;

        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new NoSuchElementException("Users list is empty.");
        }
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getSecurityUser(Constants.defaultAccessLevel);
        return globalUser;
    }
}
