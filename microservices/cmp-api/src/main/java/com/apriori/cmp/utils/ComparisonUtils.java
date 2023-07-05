package com.apriori.cmp.utils;

import com.apriori.cmp.entity.enums.CMPAPIEnum;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.request.UpdateComparison;
import com.apriori.cmp.entity.response.ErrorResponse;
import com.apriori.cmp.entity.response.GetComparisonResponse;
import com.apriori.cmp.entity.response.PostComparisonResponse;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

public class ComparisonUtils {

    /**
     * Post to the Create Comparison endpoint
     *
     * @param comparison - Create Comparison object
     * @param currentUser - UserCredentials object
     *
     * @return PostComparisonResponse
     */
    public PostComparisonResponse createComparison(CreateComparison comparison, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON, PostComparisonResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(201);

        return (PostComparisonResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Post to the Create Comparison endpoint expecting an error response
     *
     * @param comparison - Create Comparison object
     * @param currentUser - UserCredentials object
     *
     * @return PostComparisonResponse
     */
    public ErrorResponse createComparisonExpectingError(CreateComparison comparison, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON, ErrorResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(400);

        return (ErrorResponse) HTTPRequest.build(requestEntity).post().getResponseEntity();
    }

    /**
     * Update Comparison details given a Comparison ID
     *
     * @param comparisonID - String of the requested Comparison ID
     * @param comparison - Update Comparison Object
     * @param currentUser - UserCredentials object
     *
     * @return GetComparisonResponse
     */
    public GetComparisonResponse updateComparison(String comparisonID, UpdateComparison comparison, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, GetComparisonResponse.class)
                .inlineVariables(comparisonID)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(200);

        return (GetComparisonResponse) HTTPRequest.build(requestEntity).patch().getResponseEntity();
    }

    /**
     * Get Comparison details given a Comparison ID
     *
     * @param comparisonID - String of the requested Comparison ID
     * @param currentUser - UserCredentials object
     *
     * @return GetComparisonResponse
     */
    public GetComparisonResponse getComparison(String comparisonID, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, GetComparisonResponse.class)
                .inlineVariables(comparisonID)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .expectedResponseCode(200);

        return (GetComparisonResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}
