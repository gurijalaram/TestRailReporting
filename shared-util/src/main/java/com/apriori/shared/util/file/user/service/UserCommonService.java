package com.apriori.shared.util.file.user.service;

import com.apriori.shared.util.file.InitFileData;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.properties.PropertiesContext;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Contain logic to work with common users.
 *
 * @author vzarovnyi
 */
@Slf4j
public class UserCommonService {

    public static final String DEFAULT_USER_NAME = PropertiesContext.get("global.default_user_name");
    public static final String DEFAULT_PASSWORD = PropertiesContext.get("global.default_password");
    public static final String DEFAULT_ACCESS_LEVEL = PropertiesContext.get("global.default_access_level");
    private static UserCredentials globalUser;
    private static ConcurrentLinkedQueue<UserCredentials> usersQueue = initCommonUsers();

    /**
     * Return single user
     * if different.users is false
     * else each time return unique user
     *
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public static UserCredentials getUser() {
        String tokenEmail = System.getProperty("token_email");

        if (tokenEmail == null) {
            return PropertiesContext.get("global.different_users").equals("true") ? getNewUser() : getGlobalUser();
        }
        return UserCredentials.init(tokenEmail, System.getProperty("password"));
    }

    public static List<UserCredentials> initUsers() {
        List<String> users;
        String csvFileName = PropertiesContext.get("users_csv_file");
        users = InitFileData.initRows(csvFileName);
        if (users == null) {
            return Collections.singletonList(createDefaultUser());
        }
        return parseUsersToUsersCredentialsList(users);
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getNewUser();
        return globalUser;
    }

    private static UserCredentials getNewUser() {
        UserCredentials userCredentials = usersQueue.poll();
        usersQueue.add(userCredentials);

        return userCredentials;
    }

    private static List<UserCredentials> parseUsersToUsersCredentialsList(List<String> users) {
        List<UserCredentials> usersCredentials = new LinkedList<>();

        for (String line : users) {
            usersCredentials.add(initUserCredentialObject(line));
        }

        return usersCredentials;
    }

    private static UserCredentials initUserCredentialObject(String line) {
        String[] values = line.split(",");

        if (userRecordWithAccessLevel(values)) {
            return new UserCredentials(values[0], values[1], values[2]);
        } else {
            return new UserCredentials(values[0], values[1], DEFAULT_ACCESS_LEVEL);
        }
    }

    private static boolean userRecordWithAccessLevel(String[] values) {
        return values.length == 3;
    }

    private static ConcurrentLinkedQueue<UserCredentials> initCommonUsers() {
        return new ConcurrentLinkedQueue<>(initUsers());
    }

    private static UserCredentials createDefaultUser() {
        log.info(String.format("Creating default user %s/%s/%s", DEFAULT_USER_NAME, DEFAULT_PASSWORD, DEFAULT_ACCESS_LEVEL));
        return new UserCredentials(DEFAULT_PASSWORD, DEFAULT_USER_NAME, DEFAULT_ACCESS_LEVEL);
    }
}
