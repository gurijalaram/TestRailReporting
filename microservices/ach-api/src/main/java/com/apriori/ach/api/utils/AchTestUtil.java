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
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.shared.util.models.response.Users;

import org.apache.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AchTestUtil extends TestUtil {

    /**
     * Gets the special customer "aPriori Internal"
     *
     * @return The customer representing aPriori Internal
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
     * Adds or updates user preferences
     *
     * @return new object
     */
    public <T> ResponseWrapper<T> putUserPreference(String prefName, String value, Class<T> klass) {
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, klass)
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
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, null)
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
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, SuccessUpdatePreferencesResponse.class)
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
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.CUSTOMER_USERS, Users.class)
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
        RequestEntity requestEntity = RequestEntityUtil.init(ACHAPIEnum.USER_PREFERENCES, UserPreferences.class)
            .expectedResponseCode(HttpStatus.SC_OK)
            .queryParams(new QueryParams().use("sortBy[DESC]", "createdAt"));
        return HTTPRequest.build(requestEntity).get();
    }
}