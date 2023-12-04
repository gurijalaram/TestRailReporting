package com.apriori.ats.api.utils;

import com.apriori.ats.api.models.request.AuthorizeRequest;
import com.apriori.ats.api.models.response.AuthorizationResponse;
import com.apriori.ats.api.utils.enums.AuthorizeUserEnum;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class AuthorizeUserUtil {

    /**
     * POST authorize user
     *
     * @param targetCloudContext - target cloud context
     * @param token              - token
     * @return authorization object
     */
    public static ResponseWrapper<AuthorizationResponse> authorizeUser(String targetCloudContext, String token) {

        final RequestEntity requestEntity = RequestEntityUtil_Old.init(AuthorizeUserEnum.POST_AUTHORIZE_BY_BASE_URL_SECRET, AuthorizationResponse.class)
            .body(AuthorizeRequest.builder().targetCloudContext(targetCloudContext)
                .token(token)
                .build());

        return HTTPRequest.build(requestEntity).post();
    }
}
