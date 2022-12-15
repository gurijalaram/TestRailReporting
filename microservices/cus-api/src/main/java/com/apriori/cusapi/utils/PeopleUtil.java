package com.apriori.cusapi.utils;

import com.apriori.cusapi.entity.enums.CusAppAPIEnum;
import com.apriori.cusapi.entity.request.UpdateUserPrefRequest;
import com.apriori.cusapi.entity.response.PreferenceItemsResponse;
import com.apriori.cusapi.entity.response.User;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.Map;

public class PeopleUtil {
    /**
     * GET current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.CURRENT_USER, User.class)
            .token(userCredentials.getToken());

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current user preferences
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public PreferenceItemsResponse getCurrentUserPref(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.PREFERENCES, PreferenceItemsResponse.class)
            .token(userCredentials.getToken());

        ResponseWrapper<PreferenceItemsResponse> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current user preferences with params
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public PreferenceItemsResponse getCurrentUserPrefParams(UserCredentials userCredentials,String queryName,String queryValue) {
      
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.PREFERENCES, PreferenceItemsResponse.class)
            .queryParams(new QueryParams().use(queryName, queryValue))
            .token(userCredentials.getToken());

        ResponseWrapper<PreferenceItemsResponse> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * UPDATE (PATCH) current user preferences
     *
     * @param userCredentials       - the user credentials
     * @param updateUserPrefRequest - body of request
     * @return user object
     */
    public void updateCurrentUserPref(UserCredentials userCredentials, UpdateUserPrefRequest updateUserPrefRequest) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.PREFERENCES, null)
            .token(userCredentials.getToken())
            .body(updateUserPrefRequest)
            .expectedResponseCode(HttpStatus.SC_OK);
        HTTPRequest.build(requestEntity).patch();
    }

    /**
     * check if string is a number, if yes increase by 1 or decrease by one if value is min 5
     * (to avoid infinity increase of decimal place)
     *
     * @param stringValue
     * @return updated string
     */
    public String ifNumberChangeQty(String stringValue) {
        int newValue;
        if (StringUtils.isNumeric(stringValue)) {
            int intValue = Integer.parseInt(stringValue);
            if (intValue < 5) {
                newValue = intValue + 1;
            } else {
                newValue = intValue - 1;
            }

            return Integer.toString(newValue);
        } else {
            return "test";
        }
    }
}
