package com.apriori.cis.api.controller;

import com.apriori.cis.api.enums.CisAPIEnum;
import com.apriori.cis.api.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;

/**
 * The type Cis component resources.
 */
public class CisComponentResources {

    /**
     * Submit post request to get assigned components for a user
     *
     * @param currentUser              - UserCredentials
     * @param assignedComponentRequest - AssignedComponentRequest object
     * @param responseClass            - Response Class
     * @param httpStatus               - http status code
     * @param <T>                      - Response class of type T
     * @return response class
     */
    public static <T> T postToGetAssignedComponents(UserCredentials currentUser, AssignedComponentRequest assignedComponentRequest, Class<T> responseClass, Integer httpStatus) {
        RequestEntity requestEntity = RequestEntityUtil_Old.init(CisAPIEnum.USER_ASSIGNED_COMPONENTS, responseClass)
            .body(assignedComponentRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }
}


