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
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtilBuilder;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.interfaces.EndpointEnum;
import com.apriori.shared.util.interfaces.Paged;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.models.response.User;
import com.apriori.shared.util.models.response.UserProfile;
import com.apriori.shared.util.models.response.Users;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AchTestUtil extends TestUtil {

    protected RequestEntityUtil requestEntityUtil;

    @BeforeEach
    public void init() {
        requestEntityUtil = RequestEntityUtilBuilder
            .useRandomUser("admin")
            .useTokenInRequests();
    }


    /**
     * Calls an api with the GET verb.
     *
     * @param apiEnum         The api enum that specifies the endpoint.
     * @param klass           The returning class object.
     * @param inlineVariables Options variables used to help build the endpoint string.
     * @param <E>             The api enum type
     * @param <T>             The data type expected to be returned.
     * @return The response wrapper that contains the response data.
     */
    @Override
    public <E extends EndpointEnum, T> ResponseWrapper<T> getCommonRequest(E apiEnum, Class<T> klass, Integer expectedResponseCode, String... inlineVariables) {
        RequestEntity request = requestEntityUtil.init(apiEnum, klass).inlineVariables(inlineVariables).expectedResponseCode(expectedResponseCode);
        return HTTPRequest.build(request).get();
    }

    /**
     * Invokes a search on an api.
     *
     * @param apiEnum         The enum to invoke the search on.
     * @param klass           The class return value.
     * @param filter          The filter parameters
     * @param sort            The sort parameters
     * @param pageNumber      What page to retrieve
     * @param pageSize        What the page size is
     * @param inlineVariables The optional inline variables
     * @param <E>             The api enum type
     * @param <T>             The paginated data type to return
     * @return The pagination for the given klass.
     */
    // TODO vzarovnyi: fix with TestUtil to the new approach
    @Override
    public <E extends EndpointEnum, T, P extends Paged<T>> ResponseWrapper<P> find(
        E apiEnum,
        Class<P> klass,
        Map<String, ?> filter,
        Map<String, String> sort,
        int pageNumber,
        int pageSize,
        String... inlineVariables) {

        Map<String, String> pagination = new HashMap<>();
        pagination.put("pageNumber", String.format("%d", pageNumber));
        pagination.put("pageSize", String.format("%d", pageSize));

        RequestEntity request = requestEntityUtil.init(apiEnum, klass)
            .inlineVariables(inlineVariables)
            .expectedResponseCode(HttpStatus.SC_OK)
            .urlParams(Arrays.asList(filter, sort, pagination));

        return HTTPRequest.build(request).get();
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
            throw new IllegalStateException(String.format("Customer, aPriori Internal, is missing.  The data set is corrupted."));
        }

        return customer;
    }

    /**
     * Adds or updates user preferences
     *
     * @return new object
     */
    public <T> ResponseWrapper<T> putUserPreference(String prefName, String value, Class<T> klass) {
        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, klass)
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
        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, null)
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
        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, SuccessUpdatePreferencesResponse.class)
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
     * Get sorted preferences
     *
     * @return object ResponseWrapper
     */
    public ResponseWrapper<UserPreferences> getSortedUserPreferences() {
        RequestEntity requestEntity = requestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, UserPreferences.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("sortBy[DESC]", "createdAt"));
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
    public <T> ResponseWrapper<T> createNewUser(Class<T> klass, String customerIdentity, String userName, String domain, Integer expectedResponseCode, RequestEntityUtil requestEntityUtilUser) {
        User requestBody = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreateUserData.json").getPath(), User.class);
        requestBody.setUsername(userName);
        requestBody.setEmail(userName + "@" + domain + ".com");
        requestBody.getUserProfile().setGivenName(userName);
        RequestEntity requestEntity = requestEntityUtilUser.init(ACHAPIEnum.CUSTOMER_USERS, klass)
            .inlineVariables(customerIdentity)
            .expectedResponseCode(expectedResponseCode)
            .body("user", requestBody);

        return HTTPRequest.build(requestEntity).post();
    }

    public <T> ResponseWrapper<T> createNewUser(Class<T> klass, String customerIdentity, String userName, String domain, Integer expectedResponseCode) {
        return createNewUser(klass, customerIdentity, userName, domain, expectedResponseCode, requestEntityUtil);
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

    public <T> ResponseWrapper<T> patchUser(Class<T> klass, User user, String updatedJobTitle, Integer expectedResponseCode) {
        return patchUser(klass, user, updatedJobTitle, expectedResponseCode, requestEntityUtil);
    }
}