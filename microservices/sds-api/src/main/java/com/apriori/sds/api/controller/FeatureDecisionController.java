package com.apriori.sds.api.controller;

import com.apriori.sds.api.enums.SDSAPIEnum;
import com.apriori.sds.api.models.response.FeatureDecisionsResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

public class FeatureDecisionController {

    private static UserCredentials testingUser = UserUtil.getUser();

    /**
     * Call the feature decisions GET endpoint
     *
     * @return response object
     */
    public ResponseWrapper<FeatureDecisionsResponse> getFeatureDecisions() {
        final RequestEntity requestEntity =
            new RequestEntityUtil().init(SDSAPIEnum.FEATURE_DECISIONS,
                FeatureDecisionsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(testingUser.getEmail()));

        return HTTPRequest.build(requestEntity).get();
    }
}
