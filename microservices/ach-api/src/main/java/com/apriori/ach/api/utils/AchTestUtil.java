package com.apriori.ach.api.utils;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.response.AchErrorResponse;
import com.apriori.ach.api.models.response.CustomerAch;
import com.apriori.ach.api.models.response.CustomersAch;
import com.apriori.shared.util.enums.RolesEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.Enablements;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.UserProfile;
import com.apriori.shared.util.models.response.Users;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AchTestUtil extends TestUtil {

    private RequestEntityUtil requestEntityUtil;

    public AchTestUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Gets the special customer by name
     *
     * @return The customer
     */
    public CustomerAch getAprioriInternal() {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name[EQ]", "aPriori Internal");

        CustomerAch customer = findFirst(ACHAPIEnum.CUSTOMERS, CustomersAch.class, filters, Collections.emptyMap());

        if (customer == null) {
            throw new IllegalStateException("Customer, aPriori Internal, is missing.  The data set is corrupted.");
        }

        return customer;
    }

    /**
     * @param param           - name of parameter
     * @param value           - value of parameter
     * @param inlineVariables - inline variables
     * @return object ResponseWrapper
     */
    public ResponseWrapper<Users> getUsersWithParams(String param, String value, String... inlineVariables) {
        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.CUSTOMER_USERS, Users.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use(param, value));
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Creates a new user
     *
     * @param klass                - class
     * @param customerIdentity     - the customer identity
     * @param userName             - user name
     * @param domain               - domain
     * @param expectedResponseCode - response code
     * @return ResponseWrapper T
     */
    public <T> ResponseWrapper<T> createNewUser(String fileName, String customerIdentity, String userName, String domain, Integer expectedResponseCode, Class<T> klass) {
        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile(fileName).getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);

        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.CUSTOMER_USERS, klass)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates the existing user
     *
     * @param klass                - class
     * @param user                 - user response
     * @param updatedJobTitle      - string job title
     * @param expectedResponseCode - response code
     * @return ResponseWrapper T
     */
    public <T> ResponseWrapper<T> patchUser(Class<T> klass, User user, String updatedJobTitle, Integer expectedResponseCode, RequestEntityUtil requestEntityUtilUser) {
        RequestEntity requestEntity = requestEntityUtilUser.init(ACHAPIEnum.USER_BY_ID, klass)
            .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
            .expectedResponseCode(expectedResponseCode)
            .body("user",
                User.builder()
                    .userProfile(UserProfile.builder()
                        .jobTitle(updatedJobTitle).build())
                    .build());
        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Updates the existing user
     *
     * @param klass                - class
     * @param user                 - user response
     * @param updatedJobTitle      - string job title
     * @param expectedResponseCode - response code
     * @return ResponseWrapper T
     */
    public <T> ResponseWrapper<T> patchUser(Class<T> klass, User user, String updatedJobTitle, Integer expectedResponseCode) {
        return patchUser(klass, user, updatedJobTitle, expectedResponseCode, requestEntityUtil);
    }

    /**
     * Calls an API with DELETE verb
     *
     * @param expectedResponseCode - the expected response code
     * @param inlineVariables      - the variables
     * @return new object
     */
    public AchErrorResponse deleteUser(Integer expectedResponseCode, String... inlineVariables) {
        RequestEntity deleteRequest = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(expectedResponseCode)
            .inlineVariables(inlineVariables);

        ResponseWrapper<AchErrorResponse> errorResponse = HTTPRequest.build(deleteRequest).delete();

        return errorResponse.getResponseEntity();
    }

    /**
     * Calls an API with PATCH verb
     *
     * @param inlineVariables - the variables
     * @return new object
     */
    public AchErrorResponse patchEnablements(String... inlineVariables) {
        RequestEntity updateEnablements = requestEntityUtil.init(ACHAPIEnum.USER_BY_ID, AchErrorResponse.class)
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .inlineVariables(inlineVariables)
            .body("user",
                User.builder()
                    .enablements(Enablements.builder()
                        .customerAssignedRole(RolesEnum.APRIORI_DEVELOPER.getRole())
                        .userAdminEnabled(false).build())
                    .build());
        ResponseWrapper<AchErrorResponse> response = HTTPRequest.build(updateEnablements).patch();

        return response.getResponseEntity();
    }
}