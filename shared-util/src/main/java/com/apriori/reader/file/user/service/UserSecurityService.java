package com.apriori.reader.file.user.service;

import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Contain logic to work with users by access level.
 *
 * @author vzarovnyi
 */
public class UserSecurityService {

    private static UserCredentials globalUser;
    private static Map<String, Queue<UserCredentials>> usersByAccessLevel;

    /**
     * Return single user
     * if different.users is false
     * else each time return unique user
     *
     * @param accessLevel - user's access level
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser(String accessLevel) {
        return PropertiesContext.get("global.different_users").equals("true") ? getSecurityUser(accessLevel) : getGlobalUser();
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        return globalUser = getSecurityUser(PropertiesContext.get("default_access_level"));
    }

    private static synchronized UserCredentials getSecurityUser(String security) {
        Queue<UserCredentials> users = getUsersByAccessLevel().get(security);
        UserCredentials userCredentials = users.poll();
        users.add(userCredentials);

        return userCredentials;
    }

    private static Map<String, Queue<UserCredentials>> getUsersByAccessLevel() {
        if (usersByAccessLevel != null) {
            return usersByAccessLevel;
        }

        return usersByAccessLevel = initUsersWithAccessLevels();
    }

    private static Map<String, Queue<UserCredentials>> initUsersWithAccessLevels() {
        Map<String, Queue<UserCredentials>> users = new HashMap<>();
        UserCommonService.initUsers().forEach(user -> insertUserIntoAccessLevelQueue(user, users));

        return users;
    }

    private static void insertUserIntoAccessLevelQueue(UserCredentials user, Map<String, Queue<UserCredentials>> users) {
        String securityLevel = user.getAccessLevel();

        if (users.containsKey(securityLevel)) {
            users.get(securityLevel).add(user);
        } else {
            users.put(securityLevel, new LinkedList<>(Collections.singleton(user)));
        }
    }
}
