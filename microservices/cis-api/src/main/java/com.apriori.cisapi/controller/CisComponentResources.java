package com.apriori.cisapi.controller;

import com.apriori.cisapi.entity.enums.CisAPIEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

public class CisComponentResources {
    /**
     * Gets assigned components.
     *
     * @param <T>         the type parameter
     * @param currentUser the current user
     * @param klass       the klass
     * @param httpStatus  the http status
     * @return the assigned components
     */
    public static <T> T getAssignedComponents(UserCredentials currentUser, Class<T> klass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.USER_ASSIGNED_COMPONENTS, klass)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}


