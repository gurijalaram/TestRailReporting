package com.apriori.utils.users.service;

import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Work with users list file.
 * The users list file name, is declared by users.csv.file property.
 * users.csv.file: the name of csv file with users list by path: resources/{@link com.apriori.utils.constants.Constants#environment} folder
 * (if users are absent, return default user with:
 * - username:{@link com.apriori.utils.constants.Constants#defaultUserName}
 * - password:{@link com.apriori.utils.constants.Constants#defaultPassword}
 * - accessLevel:{@link com.apriori.utils.constants.Constants#defaultAccessLevel}
 * )
 *
 * @author vzarovnyi
 */
class InitUsersData {

    private static final Logger logger = LoggerFactory.getLogger(InitUsersData.class);

    static Map<String, Queue<UserCredentials>> initUsersWithAccessLevels() {
        Map<String, Queue<UserCredentials>> users = new HashMap<>();

        initUsers().forEach(user -> insertUserIntoAccessLevelQueue(user, users));

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

    static ConcurrentLinkedQueue<UserCredentials> initCommonUsers() {
        return new ConcurrentLinkedQueue<>(initUsers());
    }

    private static List<UserCredentials> initUsers() {
        InputStream usersListStream = Util.getResourceFileStream(initPathToFileWithUsers());

        if (fileNotExist(usersListStream)) {
            return Collections.singletonList(logErrorAndCreateDefaultUser("File with users list not found."));
        }

        return parseUsersToUsersCredentialsList(usersListStream);
    }

    private static boolean fileNotExist(InputStream usersListStream) {
        return usersListStream == null;
    }

    private static List<UserCredentials> parseUsersToUsersCredentialsList(InputStream usersFileStream) {
        List<UserCredentials> users = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(usersFileStream))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                users.add(initUserCredentialObject(line));
            }
        } catch (IOException e) {
            return Collections.singletonList(logErrorAndCreateDefaultUser("Error when initializing users. \n" + e.getMessage()));
        }

        return users;
    }

    private static UserCredentials initUserCredentialObject(String line) {
        String[] values = line.split(",");

        if (userRecordWithAccessLevel(values)) {
            return new UserCredentials(values[0], values[1], values[2]);
        } else {
            return new UserCredentials(values[0], values[1], Constants.defaultAccessLevel);
        }
    }

    private static boolean userRecordWithAccessLevel(String[] values) {
        return values.length == 3;
    }

    private static UserCredentials logErrorAndCreateDefaultUser(String errorText) {
        logger.error(errorText);

        return createDefaultUser();
    }

    private static UserCredentials createDefaultUser() {
        logger.info(String.format("Creating default user %s/%s/%s", Constants.defaultUserName, Constants.defaultPassword, Constants.defaultAccessLevel));

        return new UserCredentials(Constants.defaultUserName, Constants.defaultPassword, Constants.defaultAccessLevel);
    }

    private static String initPathToFileWithUsers() {

        return Constants.environment +
            "/" +
            Constants.usersFile +
            ".csv";
    }

}
