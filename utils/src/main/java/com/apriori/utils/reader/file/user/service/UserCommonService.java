package com.apriori.utils.reader.file.user.service;

import com.apriori.utils.constants.CommonConstants;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.InitFileData;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class UserCommonService {

    private static final Logger logger = LoggerFactory.getLogger(UserCommonService.class);
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

    static List<UserCredentials> initUsers() {
        List<String> users;
        users = InitFileData.initRows(PropertiesContext.get("global.users_csv_file"));
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
            return new UserCredentials(values[0], values[1], CommonConstants.DEFAULT_ACCESS_LEVEL);
        }
    }

    private static boolean userRecordWithAccessLevel(String[] values) {
        return values.length == 3;
    }

    private static ConcurrentLinkedQueue<UserCredentials> initCommonUsers() {
        return new ConcurrentLinkedQueue<>(initUsers());
    }

    private static UserCredentials createDefaultUser() {
        logger.info(String.format("Creating default user %s/%s/%s", CommonConstants.DEFAULT_USER_NAME, CommonConstants.DEFAULT_PASSWORD, CommonConstants.DEFAULT_ACCESS_LEVEL));
        return new UserCredentials(CommonConstants.DEFAULT_USER_NAME, CommonConstants.DEFAULT_PASSWORD, CommonConstants.DEFAULT_ACCESS_LEVEL);
    }
}
