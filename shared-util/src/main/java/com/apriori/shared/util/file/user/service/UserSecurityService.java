package com.apriori.shared.util.file.user.service;

import com.apriori.shared.util.enums.RolesEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.properties.PropertiesContext;

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
     * @param role - user's role
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser(RolesEnum role) {
        String tokenEmail = System.getProperty("token_email");

        if (tokenEmail == null) {
            return PropertiesContext.get("global.different_users").equals("true") ? getSecurityUser(role) : getGlobalUser();
        }
        return UserCredentials.init(tokenEmail, System.getProperty("password"));
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        return globalUser = getSecurityUser(RolesEnum.valueOf(PropertiesContext.get("default_role")));
    }

    private static synchronized UserCredentials getSecurityUser(RolesEnum security) {
        Queue<UserCredentials> users = getUsersByRole().get(security);
        UserCredentials userCredentials = users.poll();
        users.add(userCredentials);

        return userCredentials;
    }

    private static Map<String, Queue<UserCredentials>> getUsersByRole() {
        if (usersByAccessLevel != null) {
            return usersByAccessLevel;
        }

        return usersByAccessLevel = initUsersWithRoles();
    }

    private static Map<String, Queue<UserCredentials>> initUsersWithRoles() {
        Map<String, Queue<UserCredentials>> users = new HashMap<>();
        UserCommonService.initUsers().forEach(user -> insertUserIntoRoleQueue(user, users));

        return users;
    }

    private static void insertUserIntoRoleQueue(UserCredentials user, Map<String, Queue<UserCredentials>> users) {
        String securityLevel = user.getRole();

        if (users.containsKey(securityLevel)) {
            users.get(securityLevel).add(user);
        } else {
            users.put(securityLevel, new LinkedList<>(Collections.singleton(user)));
        }
    }
}
