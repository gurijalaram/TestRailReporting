package com.apriori.utils.users;

import com.apriori.utils.users.service.UserCommonService;
import com.apriori.utils.users.service.UserSecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

    public static UserCredentials getUser() {
        UserCredentials user = UserCommonService.getUser();
        logInfo(user);

        return user;
    }

    public static UserCredentials getUser(String accessLevel){
        UserCredentials user = UserSecurityService.getUser(accessLevel);
        logInfo(user);

        return user;
    }

    private static void logInfo(UserCredentials user) {
        logger.info(String.format("Received for tests USERNAME:%s PASSWORD:%s ACCESS_LEVEL:%s", user.getUsername(), user.getPassword(), user.getSecurityLevel()));
    }

}
