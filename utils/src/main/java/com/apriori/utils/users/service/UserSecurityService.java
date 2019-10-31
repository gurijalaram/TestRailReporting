package com.apriori.utils.users.service;

import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserCredentials;

import org.apache.commons.collections4.MultiValuedMap;

import java.util.Iterator;

public class UserSecurityService {

    private static MultiValuedMap<String, UserCredentials> usersByAccessLevel = InitUsersData.initUsersWithAccessLevels();
    private static UserCredentials globalUser;


    public static UserCredentials getUser(String accessLevel) {

        return Constants.useDifferentUsers ? getSecurityUser(accessLevel) : getGlobalUser();
    }

    private static UserCredentials getSecurityUser(String security) {
        Iterator<UserCredentials> securityUsers = usersByAccessLevel.get(security).iterator();

        UserCredentials user = securityUsers.next();
        securityUsers.remove();

        return user;
    }

    private static UserCredentials getGlobalUser() {
        if (globalUser != null) {
            return globalUser;
        }

        globalUser = getSecurityUser(Constants.defaultAccessLevel);
        return globalUser;
    }
}
