package com.apriori.ach.api.utils;

import com.apriori.ach.api.enums.ACHAPIEnum;
import com.apriori.ach.api.models.request.UserPreferencesRequest;
import com.apriori.ach.api.models.response.CustomerAch;
import com.apriori.ach.api.models.response.CustomersAch;
import com.apriori.ach.api.models.response.SuccessUpdatePreferencesResponse;
import com.apriori.ach.api.models.response.UserPreference;
import com.apriori.ach.api.models.response.UserPreferences;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.UserProfile;
import com.apriori.shared.util.models.response.Users;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AchTestUtil extends TestUtil {

    /**
     * Gets the special customer by name
     *
     * @param name - customer name
     * @return The customer
     */
    public CustomerAch getCustomer(String name) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("name[EQ]", name);

        CustomerAch customer = findFirst(ACHAPIEnum.CUSTOMERS, CustomersAch.class, filters, Collections.emptyMap());

        if (customer == null) {
            throw new IllegalStateException(String.format("Customer, %s, is missing.  The data set is corrupted.", name));
        }

        return customer;
    }

    /**
     * Adds or updates user preferences
     *
     * @return new object
     */
    public <T> ResponseWrapper<T> putUserPreference(String prefName, String value, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ACHAPIEnum.USER_PREFERENCES, klass)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(UserPreferencesRequest.builder()
                .userPreferences(Collections.singletonList(UserPreference.builder()
                    .name(prefName)
                    .value(value)
                    .type("INTEGER")
                    .createdBy("#SYSTEM00000")
                    .build()))
                .build());
        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * Updates user preference
     *
     * @param identity - preference identity
     * @param value    - new value of preference
     * @return new object
     */
    public ResponseWrapper<String> updatePreferencesByPatch(String identity, String value) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ACHAPIEnum.USER_PREFERENCES, null)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body("userPreferences",
                Collections.singletonMap(identity, value));

        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * Updates user preference
     *
     * @param prefName - name of preference
     * @param value    - value of preference
     * @return new object
     */
    public ResponseWrapper<SuccessUpdatePreferencesResponse> updatePreferencesByPut(String prefName, String value) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ACHAPIEnum.USER_PREFERENCES, SuccessUpdatePreferencesResponse.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .body(UserPreferencesRequest.builder()
                .userPreferences(Collections.singletonList(UserPreference.builder()
                    .name(prefName)
                    .value(value)
                    .type("INTEGER")
                    .updatedBy("#SYSTEM00000")
                    .build()))
                .build());

        return HTTPRequest.build(requestEntity).put();
    }

    /**
     * @param param - name of parameter
     * @param value - value of parameter
     * @param inlineVariables - inline variables
     * @return object ResponseWrapper
     */
    public ResponseWrapper<Users> getUsersWithParams(String param, String value, String... inlineVariables) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ACHAPIEnum.CUSTOMER_USERS, Users.class)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use(param, value));
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Get sorted preferences
     *
     * @return object ResponseWrapper
     */
    public ResponseWrapper<UserPreferences> getSortedUserPreferences() {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(ACHAPIEnum.USER_PREFERENCES, UserPreferences.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("sortBy[DESC]", "createdAt"));
        return HTTPRequest.build(requestEntity).get();
    }

    /**
     * Creates a new user
     *
     * @param klass - class
     * @param customerIdentity - the customer identity
     * @param userName - user name
     * @param domain - domain
     * @param expectedResponseCode - response code
     * @return ResponseWrapper T
     */
    public <T> ResponseWrapper<T> createNewUser(Class<T> klass, String customerIdentity, String userName, String domain, Integer expectedResponseCode) {
        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.CUSTOMER_USERS, klass)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    /**
     * Updates the existing user
     *
     * @param klass - class
     * @param user - user response
     * @param updatedJobTitle - string job title
     * @param expectedResponseCode - response code
     * @return ResponseWrapper T
     */
    public <T> ResponseWrapper<T> patchUser(Class<T> klass, User user, String updatedJobTitle, Integer expectedResponseCode) {
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.USER_BY_ID, klass)
            .inlineVariables(user.getCustomerIdentity(), user.getIdentity())
            .expectedResponseCode(expectedResponseCode)
            .body("user",
                User.builder()
                    .userProfile(UserProfile.builder()
                        .jobTitle(updatedJobTitle).build())
                    .build());
        return HTTPRequest.build(requestEntity).patch();
    }
}