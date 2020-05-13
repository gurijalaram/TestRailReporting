package com.apriori.utils.users.service;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Contain logic to work with common users.
 *
 * @author vzarovnyi
 */
public class UserCommonService {

    private static final Logger logger = LoggerFactory.getLogger(UserCommonService.class);

    private static ConcurrentLinkedQueue<UserCredentials> usersQueue = InitUsersData.initCommonUsers();
    private static UserCredentials globalUser;

    /**
     * Return single user
     * if in {@link com.apriori.utils.constants.Constants#environment}.properties file different.users is false
     * else each time return unique user
     *
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser() {
        return Constants.useDifferentUsers ? getNewUser() : getGlobalUser();
    }

    private static UserCredentials getNewUser() {
        UserCredentials userCredentials = usersQueue.poll();
        usersQueue.add(userCredentials);

        return userCredentials;
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getNewUser();
        return globalUser;
    }
}
