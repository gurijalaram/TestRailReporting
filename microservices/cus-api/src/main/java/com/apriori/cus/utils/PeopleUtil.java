package com.apriori.cus.utils;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

import com.apriori.cus.enums.CusAppAPIEnum;
import com.apriori.cus.models.request.UpdateUserPrefRequest;
import com.apriori.cus.models.request.UpdateUserRequest;
import com.apriori.cus.models.response.ErrorResponse;
import com.apriori.cus.models.response.PreferenceItemsResponse;
import com.apriori.cus.models.response.User;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.QueryParams;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.HashMap;

@Slf4j
public class PeopleUtil {

    /**
     * GET current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public User getCurrentUser(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.CURRENT_USER, User.class)
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()));

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * UPDATE (PATCH) current user
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public User updateCurrentUser(UserCredentials userCredentials, UpdateUserRequest updateUserRequest) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.CURRENT_USER, User.class)
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
            .body("user",
                updateUserRequest);

        ResponseWrapper<User> userResponse = HTTPRequest.build(requestEntity).patch();
        return userResponse.getResponseEntity();
    }

    /**
     * UPDATE (PATCH) current user - bad request
     *
     * @param userCredentials - the user credentials
     * @return ErrorResponse object
     */
    public ErrorResponse updateCurrentUserBadRequest(UserCredentials userCredentials, UpdateUserRequest updateUserRequest) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.CURRENT_USER, ErrorResponse.class)
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
            .body("user",
                updateUserRequest)
            .expectedResponseCode(SC_BAD_REQUEST);
        ResponseWrapper<ErrorResponse> errorResponse = HTTPRequest.build(requestEntity).patch();
        return errorResponse.getResponseEntity();
    }

    /**
     * GET current user preferences
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public PreferenceItemsResponse getCurrentUserPref(UserCredentials userCredentials) {
        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.PREFERENCES, PreferenceItemsResponse.class)
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()));

        ResponseWrapper<PreferenceItemsResponse> userResponse = HTTPRequest.build(requestEntity).get();
        return userResponse.getResponseEntity();
    }

    /**
     * GET current user preferences with params
     *
     * @param userCredentials - the user credentials
     * @return user object
     */
    public PreferenceItemsResponse getCurrentUserPrefParams(UserCredentials userCredentials, String queryName, String queryValue) {

        final RequestEntity requestEntity = RequestEntityUtil.init(CusAppAPIEnum.PREFERENCES, PreferenceItemsResponse.class)
            .queryParams(new QueryParams().use(queryName, queryValue))
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()));

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
            .headers(new HashMap<String, String>() {
                {
                    put("ap-cloud-context", userCredentials.generateCloudContext().getCloudContext());
                }
            })
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()))
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
