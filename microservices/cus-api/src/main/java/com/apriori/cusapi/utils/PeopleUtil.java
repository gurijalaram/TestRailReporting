package com.apriori.cusapi.utils;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

import com.apriori.cusapi.entity.enums.CusAppAPIEnum;
import com.apriori.cusapi.entity.request.UpdateUserPrefRequest;
import com.apriori.cusapi.entity.request.UpdateUserRequest;
import com.apriori.cusapi.entity.response.ErrorResponse;
import com.apriori.cusapi.entity.response.PreferenceItemsResponse;
import com.apriori.cusapi.entity.response.User;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.QueryParams;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;

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
