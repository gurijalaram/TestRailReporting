package com.apriori.cds.api.utils;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

import com.apriori.cds.api.enums.CDSAPIEnum;
import com.apriori.cds.api.models.request.UpdateCredentials;
import com.apriori.cds.api.models.response.CredentialsItems;
import com.apriori.cds.api.models.response.ErrorResponse;
import com.apriori.cds.api.models.response.UserRole;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.UserProfile;
import com.apriori.shared.util.models.response.Users;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpStatus;

public class CdsUserUtil {
    private RequestEntityUtil requestEntityUtil;

    public CdsUserUtil(RequestEntityUtil requestEntityUtil) {
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * GET user by email
     *
     * @param email email of the user
     * @return response object
     */
    public ResponseWrapper<Users> getUserByEmail(String email) {

        final RequestEntity requestEntity =
            requestEntityUtil.init(CDSAPIEnum.USERS, Users.class)
                .queryParams(new QueryParams().use("email[EQ]", email))
                .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * PATCH call to update the user credentials
     *
     * @param customerIdentity    - the customer id
     * @param userIdentity        - the user id
     * @param passwordHashCurrent - current hash password
     * @param passwordSalt        - the salt password
     * @return new object
     */
    public ResponseWrapper<CredentialsItems> updateUserCredentials(
        String customerIdentity,
        String userIdentity,
        String passwordHashCurrent,
        String passwordSalt) {

        RequestEntity requestEntity = requestEntityUtil
            .init(CDSAPIEnum.USER_CREDENTIALS_BY_CUSTOMER_USER_IDS, CredentialsItems.class)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(
                "userCredential",
                UpdateCredentials.builder()
                    .currentPasswordHash(passwordHashCurrent)
                    .newPasswordHash(new GenerateStringUtil().getHashPassword())
                    .newPasswordSalt(passwordSalt)
                    .newEncryptedPassword(new GenerateStringUtil().getRandomStringSpecLength(12).toLowerCase())
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * POST call to add a customer
     *
     * @param customerIdentity - the customer id
     * @param userName         - the username
     * @param domain           - the customer name
     * @return new object
     */
    public ResponseWrapper<User> addUser(String customerIdentity, String userName, String domain) {

        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, User.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Creates user with set of enablements
     *
     * @param customerIdentity     - the customer id
     * @param userName             - the username
     * @param domain               - the customer name
     * @param customerAssignedRole - the customer assigned role
     * @return new object
     */
    public ResponseWrapper<User> addUserWithEnablements(String customerIdentity, String userName, String domain, String customerAssignedRole) {

        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserWithEnablementsData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        requestBody.getEnablements().setCustomerAssignedRole(customerAssignedRole);
        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.CUSTOMER_USERS, User.class)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(HttpStatus.SC_CREATED)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * PATCH call to update a user
     *
     * @param user - the user
     * @return new object
     */
    public ResponseWrapper<User> patchUser(User user) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, User.class)
            .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(
                "user",
                User.builder()
                    .identity(user.getIdentity())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .createdBy(user.getCreatedBy())
                    .active(user.getActive())
                    .userProfile(UserProfile.builder()
                        .department("Design Dept")
                        .supervisor("Moya Parker").build())
                    .build()
            );

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * PATCH call to update a user
     *
     * @param <T>                  - the class
     * @param customerIdentity     - the customer id
     * @param userIdentity         - the user id
     * @param expectedResponseCode - the expected response code
     * @param user                 - the user
     * @return new object
     */
    public <T> ResponseWrapper<T> patchUser(
        Class<T> klass,
        String customerIdentity,
        String userIdentity,
        Integer expectedResponseCode,
        JsonNode user) {

        RequestEntity requestEntity = requestEntityUtil.init(CDSAPIEnum.USER_BY_CUSTOMER_USER_IDS, klass)
            .inlineVariables(customerIdentity, userIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("user", user);

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Creates role for a user
     *
     * @param role            - the role
     * @param inlineVariables - inline variables
     * @return new object
     */
    public UserRole createRoleForUser(String role, String... inlineVariables) {
        RequestEntity requestEntity = createRole(role, SC_CREATED, UserRole.class, inlineVariables);
        ResponseWrapper<UserRole> response = HTTPRequest.build(requestEntity).post();

        return response.getResponseEntity();
    }

    /**
     * Creates invalid role for a user and get error response
     *
     * @param role            - the role
     * @param inlineVariables - inline variables
     * @return new object
     */
    public ErrorResponse createInvalidRoleForUser(String role, String... inlineVariables) {
        RequestEntity requestEntity = createRole(role, SC_NOT_FOUND, ErrorResponse.class, inlineVariables);
        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).post();

        return errorResponse.getResponseEntity();
    }

    private <T> RequestEntity createRole(String role, Integer expectedStatusCode, Class<T> klass, String... inlineVariables) {
        return requestEntityUtil.init(CDSAPIEnum.USER_ROLES, klass)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(expectedStatusCode)
            .body(
                "role",
                UserRole.builder()
                    .role(role)
                    .createdBy("#SYSTEM00000")
                    .build()
            );
    }
}
