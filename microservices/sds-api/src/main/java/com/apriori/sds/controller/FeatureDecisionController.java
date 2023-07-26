package com.apriori.sds.controller;

import com.apriori.AuthUserContextUtil;
import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.sds.entity.enums.SDSAPIEnum;
import com.apriori.sds.entity.response.FeatureDecisionsResponse;

public class FeatureDecisionController extends RequestEntityUtil {

    private static UserCredentials testingUser = UserUtil.getUser();

    /**
     * Call the feature decisions GET endpoint
     *
     * @return response object
     */
    public ResponseWrapper<FeatureDecisionsResponse> getFeatureDecisions() {
        final RequestEntity requestEntity =
            init(SDSAPIEnum.FEATURE_DECISIONS,
                FeatureDecisionsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(testingUser.getEmail()));

        return HTTPRequest.build(requestEntity).get();
    }
}
