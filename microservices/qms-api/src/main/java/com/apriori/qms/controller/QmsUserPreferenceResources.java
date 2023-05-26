package com.apriori.qms.controller;

import com.apriori.qms.entity.request.userpreference.UserPreferenceRequest;
import com.apriori.qms.enums.QMSAPIEnum;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

import utils.QmsApiTestUtils;

@SuppressWarnings("unchecked")
public class QmsUserPreferenceResources {
    /**
     * Get user preference
     *
     * @param responseClass expected response class
     * @param currentUser   UserCredentials
     * @param httpStatus    expected http status code
     * @param <T>           response class type
     * @return Response class object
     */
    public static <T> T getUserPreference(Class<T> responseClass, UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, responseClass)
            .headers(QmsApiTestUtils.setUpHeader(currentUser.generateCloudContext().getCloudContext()))
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }

    /**
     * Update user preference
     *
     * @param userPreferenceRequestBuilder user preference request builder
     * @param responseClass                expected response class
     * @param currentUser                  UserCredentials
     * @param httpStatus                   expected http status code
     * @param <T>                          response class type
     * @return Response class object
     */
    public static <T> T updateUserPreference(UserPreferenceRequest userPreferenceRequestBuilder, Class<T> responseClass, UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, responseClass)
            .body(userPreferenceRequestBuilder)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Delete user preference
     *
     * @param currentUser   UserCredentials
     * @param httpStatus    expected http status code
     */
    public static void deleteUserPreference(UserCredentials currentUser, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(QMSAPIEnum.USER_PREFERENCE, null)
            .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
            .expectedResponseCode(httpStatus);

        HTTPRequest.build(requestEntity).delete();
    }
}