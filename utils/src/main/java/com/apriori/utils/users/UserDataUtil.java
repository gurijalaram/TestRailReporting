package com.apriori.utils.users;

import com.apriori.utils.Util;
import com.apriori.utils.constants.Constants;
import com.sun.tools.jxc.ap.Const;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UserDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserDataUtil.class);

    private static ConcurrentLinkedQueue<List<String>> usersQueue = new ConcurrentLinkedQueue<>();
    private static UserCredentials globalUser;

    static {
        initUsersForEnvironment();
    }

    public static UserCredentials getUser() {

        return Constants.useDifferentUsers ? getNewUser() : getGlobalUser();
    }

    public static UserCredentials getGlobalUser() {

        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getNewUser();

        return globalUser;
    }

    public static UserCredentials getNewUser() {
        try {
            List<String> credentials = usersQueue.poll();

            if (credentials == null) {
                throw new IllegalArgumentException("Empty users list");
            }

            return new UserCredentials(credentials.get(0), credentials.get(1));

        } catch (IllegalArgumentException e) {
            logger.error("Can't take user from queue. Thread info:" + Thread.currentThread().getName() + "\n"
                + e.getMessage());
            throw new IllegalArgumentException();
        }
    }


    private static void initUsersForEnvironment() {

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Util.getResourceFileStream(initPathToFileWithUsers())))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                usersQueue.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            logger.error("Error with initializing users. Users file.\n"
                + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    private static String initPathToFileWithUsers() {
        return new StringBuffer(System.getProperty(Constants.defaultEnvironmentKey))
            .append("/")
            .append(Constants.usersFile)
            .append(".csv")
            .toString();
    }
}
