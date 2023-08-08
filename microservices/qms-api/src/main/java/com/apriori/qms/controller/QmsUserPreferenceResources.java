package com.apriori.qms.controller;

import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.AuthUserContextUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.qms.models.request.userpreference.UserPreferenceRequest;
import com.apriori.qms.utils.QmsApiTestUtils;
import com.apriori.reader.file.user.UserCredentials;

/**
 * The type Qms user preference resources.
 */
public class QmsUserPreferenceResources {
    /**
     * Gets user preference.
     *
     * @param <T>           the type parameter
     * @param responseClass the response class
     * @param currentUser   the current user
     * @param httpStatus    the http status
     * @return the user preference
     */
    public static <T> T getUserPreference(Class<T> responseClass, UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).get();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Update user preference.
     *
     * @param <T>                          the type parameter
     * @param userPreferenceRequestBuilder the user preference request builder
     * @param responseClass                the response class
     * @param currentUser                  the current user
     * @param httpStatus                   the http status
     * @return the response entity
     */
    public static <T> T updateUserPreference(UserPreferenceRequest userPreferenceRequestBuilder, Class<T> responseClass, UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, responseClass)
            .body(userPreferenceRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();
        return responseWrapper.getResponseEntity();
    }

    /**
     * Delete user preference.
     *
     * @param currentUser the current user
     * @param httpStatus  the http status
     */
    public static void deleteUserPreference(UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, null)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        HTTPRequest.build(requestEntity).delete();
    }
}