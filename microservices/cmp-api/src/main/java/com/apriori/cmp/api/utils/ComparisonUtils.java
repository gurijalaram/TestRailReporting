package com.apriori.cmp.api.utils;

import com.apriori.cid.api.models.response.PersonResponse;
import com.apriori.cid.api.utils.PeopleUtil;
import com.apriori.cmp.api.models.enums.CMPAPIEnum;
import com.apriori.cmp.api.models.request.CreateComparison;
import com.apriori.cmp.api.models.request.UpdateComparison;
import com.apriori.cmp.api.models.response.GetComparisonResponse;
import com.apriori.cmp.api.models.response.GetComparisonsResponse;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.AuthUserContextUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComparisonUtils {

    /**
     * Post to the Create Comparison endpoint
     *
     * @param comparison       - Create Comparison object
     * @param currentUser      - UserCredentials object
     * @param klass            - The desired return class
     * @param expectedResponse - The expected HTTP response code
     * @return PostComparisonResponse
     */
    public <T> T createComparison(CreateComparison comparison, UserCredentials currentUser, Class<T> klass, Integer expectedResponse) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISONS, klass)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(expectedResponse);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).post();

        return responseWrapper.getResponseEntity();
    }

    /**
     * Update Comparison details given a Comparison ID
     *
     * @param inlineVariables - inline variables
     * @param comparison      - Update Comparison Object
     * @param currentUser     - UserCredentials object
     * @return GetComparisonResponse
     */
    public <T> T updateComparison(UpdateComparison comparison, UserCredentials currentUser, Class<T> klass, Integer expectedResponse, String... inlineVariables) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, klass)
                .inlineVariables(inlineVariables)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .body("comparison", comparison)
                .expectedResponseCode(expectedResponse);

        ResponseWrapper<T> responseWrapper = HTTPRequest.build(requestEntity).patch();

        return responseWrapper.getResponseEntity();
    }

    /**
     * Get Comparison details given a Comparison ID
     *
     * @param inlineVariables - inline variables
     * @param currentUser     - UserCredentials object
     * @return GetComparisonResponse
     */
    public GetComparisonResponse getComparison(UserCredentials currentUser, String... inlineVariables) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, GetComparisonResponse.class)
                .inlineVariables(inlineVariables)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .expectedResponseCode(HttpStatus.SC_OK);

        // TODO: 17/07/2023 is this really a patch or should it be a get?
        ResponseWrapper<GetComparisonResponse> responseWrapper = HTTPRequest.build(requestEntity).patch();

        return responseWrapper.getResponseEntity();
    }

    /**
     * Calls an api with GET verb
     *
     * @param currentUser - the user credentials
     * @return response object
     */
    public List<GetComparisonResponse> getComparisons(UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISONS, GetComparisonsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<GetComparisonsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        return responseWrapper.getResponseEntity().getItems();
    }

    /**
     * Calls an api and queries with GET verb
     * @param currentUser - the user credentials
     * @param urlParams - parameters for url
     * @return response object
     */
    public List<GetComparisonResponse> queryComparison(UserCredentials currentUser, String... urlParams) {
        Map<String, String> searchCriteria = new HashMap<>();
        List<String[]> uriParams = Arrays.stream(urlParams).map(o -> o.split(",")).collect(Collectors.toList());

        uriParams.forEach(uriParam -> searchCriteria.put(uriParam[0].trim(), uriParam[1].trim()));

        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISONS, GetComparisonsResponse.class)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .urlParams(Collections.singletonList(searchCriteria))
                .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<GetComparisonsResponse> responseWrapper = HTTPRequest.build(requestEntity).get();

        return responseWrapper.getResponseEntity().getItems();
    }

    /**
     * GET current person
     *
     * @param userCredentials - the user credentials
     * @return person object
     */
    public PersonResponse getCurrentPerson(UserCredentials userCredentials) {
        return new PeopleUtil().getCurrentPerson(userCredentials);
    }

    /**
     * Calls an api with DELETE verb
     *
     * @param comparisonID - String of the requested Comparison ID
     * @param currentUser  - UserCredentials object
     * @return GetComparisonResponse
     */
    public <T> ResponseWrapper<T> deleteComparison(String comparisonID, UserCredentials currentUser) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(CMPAPIEnum.COMPARISON_BY_IDENTITY, null)
                .inlineVariables(comparisonID)
                .apUserContext(new AuthUserContextUtil().getAuthUserContext(currentUser.getEmail()))
                .expectedResponseCode(HttpStatus.SC_NO_CONTENT);

        return HTTPRequest.build(requestEntity).delete();
    }
}
