package com.apriori.shared.util.file.user;

import com.apriori.shared.util.enums.apis.UsersApiEnum;
import com.apriori.shared.util.file.user.service.UserCommonService;
import com.apriori.shared.util.file.user.service.UserSecurityService;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.Users;

import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpStatus;

import java.util.List;

/**
 * Get user functionality.
 * Has reference to {@link CommonConstants}.properties file
 * reference properties:
 * - different.users
 * - if true: will return each time new user
 * - if false: will return each time single user
 * - users.csv.file: the name of csv file with users list from resources/{@link CommonConstants} folder
 * (if users are absent, return default user with:
 * - username:{@link CommonConstants#DEFAULT_USER_NAME}
 * - password:{@link CommonConstants#DEFAULT_PASSWORD}
 * )
 * <p>
 * Users list is global for two Collections:
 * - security users collection
 * - common users collection
 * <p>
 * Each collection has a copy of this list and after getting the user, this user will be pushed to the end of queue
 * Example:
 * security collection - user1, user2, user3
 * common collection - user1, user2, user3
 * <p>
 * after getting the security user
 * <p>
 * security collection - user2, user3, user1
 * common collection - user1, user2, user3
 *
 * @author vzarovnyi
 */
@Slf4j
public class UserUtil {

    /**
     * Return common user
     *
     * @return User
     */
    public static synchronized UserCredentials getUser() {
        UserCredentials user = UserCommonService.getUser();
        logInfo(user);
        return user;
    }

    /**
     * Return user by access level
     *
     * @param accessLevel - access level of needed user
     * @return User
     */
    public static UserCredentials getUser(String accessLevel) {
        UserCredentials user = UserSecurityService.getUser(accessLevel);
        logInfo(user);
        return user;
    }

    /**
     * Gets all users from current csv
     * @return list
     */
    public static List<UserCredentials> getUsers() {
        return UserCommonService.initUsers();
    }

    /**
     * Return common user with cloud context
     *
     * @return User
     */
    public static UserCredentials getUserWithCloudContext() {
        UserCredentials user = UserCommonService.getUser()
            .generateToken()
            .generateCloudContext();
        logInfo(user);
        return user;
    }

    /**
     * Gets a common user for on prem env (without token or cloud context)
     *
     * @return UserCredentials instance
     */
    public static UserCredentials getUserOnPrem() {
        UserCredentials user = UserCommonService.getUser();
        logInfo(user);
        return user;
    }

    private static void logInfo(UserCredentials user) {
        log.info(getUserLogInfo(user));
    }

    @Attachment
    public static String getUserLogInfo(UserCredentials user) {
        return String.format("Received for tests USERNAME:%s PASSWORD:%s ACCESS_LEVEL:%s", user.getEmail(), user.getPassword(), user.getAccessLevel());
    }

    /**
     * GET user by email
     *
     * @param userCredentials - the user credentials
     * @return response object
     */
    public static User getUserByEmail(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil_Old.init(UsersApiEnum.USERS, Users.class)
            .queryParams(new QueryParams().use("email[EQ]", userCredentials.getEmail()))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<Users> response = HTTPRequest.build(requestEntity).get();
        return response.getResponseEntity().getItems().stream().findFirst().get();
    }
}
