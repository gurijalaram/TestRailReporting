package com.apriori.cmp.utils;

import com.apriori.cmp.entity.enums.CMPAPIEnum;
import com.apriori.cmp.entity.request.CreateComparison;
import com.apriori.cmp.entity.request.UpdateComparison;
import com.apriori.cmp.entity.response.GetComparisonResponse;
import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.user.UserCredentials;

import org.apache.http.HttpStatus;

public class ComparisonUtils {

    /**
     * Post to the Create Comparison endpoint
     *
     * @param comparison - Create Comparison object
     * @param currentUser - UserCredentials object
     * @param klass - The desired return class
     * @param expectedResponse - The expected HTTP response code
     *
     * @return PostComparisonResponse
     */
    public <T> T createComparison(CreateComparison comparison, UserCredentials currentUser, Class<T> klass, Integer expectedResponse) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON, klass)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(expectedResponse);

        return (T) HTTPRequest.build(requestEntity).post().getResponseEntity();
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
    public <T> T updateComparison(String comparisonID, UpdateComparison comparison, UserCredentials currentUser, Class<T> klass, Integer expectedResponse) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, klass)
                .inlineVariables(comparisonID)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(expectedResponse);

        return (T) HTTPRequest.build(requestEntity).patch().getResponseEntity();
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
                .expectedResponseCode(HttpStatus.SC_OK);

        return (GetComparisonResponse) HTTPRequest.build(requestEntity).get().getResponseEntity();
    }
}
