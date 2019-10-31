package com.apriori.utils.users.service;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import java.util.concurrent.ConcurrentLinkedQueue;

public class UserCommonService {

    private static ConcurrentLinkedQueue<UserCredentials> usersQueue = InitUsersData.initCommonUsers();
    private static UserCredentials globalUser;


    public static UserCredentials getUser() {

        return Constants.useDifferentUsers ? getNewUser() : getGlobalUser();
    }

    private static UserCredentials getNewUser() {
        return usersQueue.poll();
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getNewUser();
        return globalUser;
    }
}
