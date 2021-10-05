package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.PatchCostingPreferenceRequest;
import com.apriori.bcs.entity.response.CostingPreferences;
import com.apriori.bcs.entity.response.CustomAttributes;
import com.apriori.bcs.entity.response.Customers;
import com.apriori.bcs.entity.response.DigitalFactories;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.bcs.entity.response.UserDefinedAttributes;
import com.apriori.bcs.entity.response.VPE;
import com.apriori.bcs.enums.BCSAPIEnum;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public class CustomerResources {

    public static <T> ResponseWrapper<T> getCustomers() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_CUSTOMERS, Customers.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getCostingPreferences() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_COSTING_PREFERENCES, CostingPreferences.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static CostingPreferences patchCostingPreferences(PatchCostingPreferenceRequest patchCostingPreferenceRequest) {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.PATCH_COSTING_PREFERENCES, CostingPreferences.class)
            .body(patchCostingPreferenceRequest);

        return (CostingPreferences) HTTPRequest.build(requestEntity).patch()
            .getResponseEntity();
    }

    public static <T> ResponseWrapper<T> getProcessGroups() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_PROCESS_GROUPS, ProcessGroups.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getUserDefinedAttributes() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_USER_DEFINED_ATTRIBUTES, UserDefinedAttributes.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getVirtualProductEnvironments() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_VPEs, VPE.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getDigitalFactories() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactories.class);

        return HTTPRequest.build(requestEntity).get();
    }

    public static <T> ResponseWrapper<T> getCustomAttributes() {
        RequestEntity requestEntity = RequestEntityUtil.init(BCSAPIEnum.GET_CUSTOM_ATTRIBUTES, CustomAttributes.class);

        return HTTPRequest.build(requestEntity).get();
    }

}
