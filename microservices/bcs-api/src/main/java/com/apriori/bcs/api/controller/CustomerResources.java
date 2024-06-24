package com.apriori.bcs.api.controller;

import com.apriori.bcs.api.enums.BCSAPIEnum;
import com.apriori.bcs.api.models.request.PatchCostingPreferenceRequest;
import com.apriori.bcs.api.models.response.UserPreferences;
import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.RequestEntityUtil_Old;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;

import org.apache.http.HttpStatus;

import java.util.Random;

/**
 * This class contains the methods related to customer-controller APIs
 */
public class CustomerResources {

    /**
     * Update customer patch costing preferences
     *
     * @return response  - Object of responseWrapper
     */
    public static ResponseWrapper<UserPreferences> patchCostingPreferences() {
        PatchCostingPreferenceRequest request =
            JsonManager.deserializeJsonFromInputStream(
                FileResourceUtil.getResourceFileStream("schemas/testdata/UpdateCostingPreferences.json"), PatchCostingPreferenceRequest.class);
        request.setCadToleranceReplacement(100.00);
        request.setMinCadToleranceThreshold(new Random().nextDouble());
        RequestEntity requestEntity = RequestEntityUtil_Old
            .init(BCSAPIEnum.CUSTOMER_USER_PREFERENCES, UserPreferences.class)
            .inlineVariables(SharedCustomerUtil.getCurrentCustomerIdentity())
            .body(request)
            .expectedResponseCode(HttpStatus.SC_OK);
        return HTTPRequest.build(requestEntity).patch();
    }


    /**
     * Update customer patch costing preferences with custom data sent as argument
     *
     * @return response  - Object of responseWrapper
     */
    public static ResponseWrapper<UserPreferences> patchCostingPreferences(PatchCostingPreferenceRequest patchCostingPreferenceRequest) {
        RequestEntity requestEntity = RequestEntityUtil_Old
            .init(BCSAPIEnum.CUSTOMER_USER_PREFERENCES, UserPreferences.class)
            .inlineVariables(SharedCustomerUtil.getCurrentCustomerIdentity())
            .body(patchCostingPreferenceRequest);
        return HTTPRequest.build(requestEntity).patch();
    }

    /**
     * This overloaded method is to create customer request entity.
     *
     * @param endPoint       - endpoint which is to be called
     * @param klass          - Response class
     * @return RequestEntity - Batch Part complete RequestEntity
     */
    public static <T> RequestEntity getCustomerRequestEntity(BCSAPIEnum endPoint, Class<T> klass) {
        return RequestEntityUtil_Old.init(endPoint, klass)
            .inlineVariables(SharedCustomerUtil.getCurrentCustomerIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);
    }
}
