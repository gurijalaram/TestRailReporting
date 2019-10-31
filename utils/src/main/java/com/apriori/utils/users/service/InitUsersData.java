package com.apriori.utils.users.service;

import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InitUsersData {

    private static final Logger logger = LoggerFactory.getLogger(InitUsersData.class);


    public static MultiValuedMap<String, UserCredentials> initUsersWithAccessLevels() {
        MultiValuedMap<String, UserCredentials> usersByAccessLevel = new ArrayListValuedHashMap<>();

        initUsers().forEach(user -> usersByAccessLevel.put(user.getSecurityLevel(), user));

        return usersByAccessLevel;
    }

    public static ConcurrentLinkedQueue<UserCredentials> initCommonUsers() {

        return new ConcurrentLinkedQueue<>(initUsers());
    }

    private static List<UserCredentials> initUsers() {

        List<UserCredentials> users = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Util.getResourceFileStream(initPathToFileWithUsers())))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length == 3) {
                    users.add(new UserCredentials(values[0], values[1], values[2]));
                } else {
                    users.add(new UserCredentials(values[0], values[1]));
                }
            }

            return users;

        } catch (IOException e) {
            return Collections.singletonList(logErrorAndCreateDefaultUser("Error when initializing users.", e));
        }
    }

    private static UserCredentials logErrorAndCreateDefaultUser(String errorText, Exception e) {
        logger.error(errorText + "\n"
            + e.getMessage());

        logger.info(String.format("Creating default user %s/%s", Constants.defaultUserName, Constants.defaultPassword));

        return new UserCredentials(Constants.defaultUserName, Constants.defaultPassword, Constants.defaultAccessLevel);
    }

    private static String initPathToFileWithUsers() {

        return new StringBuffer(Constants.environment)
            .append("/")
            .append(Constants.usersFile)
            .append(".csv")
            .toString();
    }

}
