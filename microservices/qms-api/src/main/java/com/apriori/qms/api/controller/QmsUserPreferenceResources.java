package com.apriori.qms.api.controller;

import com.apriori.qms.api.enums.QMSAPIEnum;
import com.apriori.qms.api.models.request.userpreference.UserPreferenceRequest;
import com.apriori.qms.api.utils.QmsApiTestUtils;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

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