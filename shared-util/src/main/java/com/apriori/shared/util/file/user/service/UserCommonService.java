package com.apriori.shared.util.file.user.service;

import com.apriori.shared.util.enums.RolesEnum;
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
    public static final RolesEnum DEFAULT_ROLE = RolesEnum.valueOf(PropertiesContext.get("global.default_role"));

    public static final Boolean IS_DIFFERENT_USERS = Boolean.valueOf(PropertiesContext.get("global.different_users"));
    public static final Boolean USE_DEFAULT_USER = Boolean.valueOf(PropertiesContext.get("global.use_default_user"));

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
            return IS_DIFFERENT_USERS ? getNewUser() : getGlobalUser();
        }
        return UserCredentials.init(tokenEmail, System.getProperty("password"));
    }

    public static List<UserCredentials> initUsers() {
        List<String> users;
        String csvFileName = PropertiesContext.get("users_csv_file");
        users = InitFileData.initRows(csvFileName);
        if (users == null || USE_DEFAULT_USER) {
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

        if (userRecordWithRole(values)) {
            return new UserCredentials(values[0], values[1], RolesEnum.valueOf(values[2]));
        } else {
            return new UserCredentials(values[0], values[1], DEFAULT_ROLE);
        }
    }

    private static boolean userRecordWithRole(String[] values) {
        return values.length == 3;
    }

    private static ConcurrentLinkedQueue<UserCredentials> initCommonUsers() {
        return new ConcurrentLinkedQueue<>(initUsers());
    }

    private static UserCredentials createDefaultUser() {
        log.info(String.format("Creating default user %s/%s/%s", DEFAULT_USER_NAME, DEFAULT_PASSWORD, DEFAULT_ROLE));
        return new UserCredentials(DEFAULT_USER_NAME, DEFAULT_PASSWORD, DEFAULT_ROLE);
    }
}
