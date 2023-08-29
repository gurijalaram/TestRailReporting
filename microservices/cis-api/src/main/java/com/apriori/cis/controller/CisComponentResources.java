package com.apriori.cis.controller;

import com.apriori.cis.enums.CisAPIEnum;
import com.apriori.cis.models.request.bidpackage.AssignedComponentRequest;
import com.apriori.cis.models.response.component.AssignedComponentsResponse;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;

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
        RequestEntity requestEntity = RequestEntityUtil.init(CisAPIEnum.USER_ASSIGNED_COMPONENTS, responseClass)
            .body(assignedComponentRequest)
            .token(currentUser.getToken())
            .expectedResponseCode(httpStatus);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }
}


