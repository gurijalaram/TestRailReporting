package com.apriori.bcs.controller;

import com.apriori.bcs.entity.request.PatchCostingPreferenceRequest;
import com.apriori.bcs.entity.response.CostingPreferences;
import com.apriori.bcs.entity.response.CustomAttributes;
import com.apriori.bcs.entity.response.Customers;
import com.apriori.bcs.entity.response.DigitalFactories;
import com.apriori.bcs.entity.response.ProcessGroups;
import com.apriori.bcs.entity.response.UserDefinedAttributes;
import com.apriori.bcs.entity.response.VPE;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;

public class CustomerResources extends CisBase {

    private static final String endpointCostingPreferences = "costing-preferences";
    private static final String endpointProcessGroups = "process-groups";
    private static final String endpointUserDefinedAttributes = "user-defined-attributes";
    private static final String endpointVPE = "virtual-production-environments";
    private static final String endpointDigitalFactories = "digital-factories";
    private static final String endpointCustomAttributes = "custom-attributes";

    public static <T>  ResponseWrapper<T> getCustomers() {
        String url = String.format(getBaseCisUrl(), "");
        return GenericRequestUtil.get(
                RequestEntity.init(url, Customers.class),
                new RequestAreaApi()
        );
    }

    public static <T>  ResponseWrapper<T> getCostingPreferences() {
        String url = String.format(getCisUrl(), endpointCostingPreferences);
        return GenericRequestUtil.get(
                RequestEntity.init(url, CostingPreferences.class),
                new RequestAreaApi()
        );
    }

    public static CostingPreferences patchCostingPreferences(PatchCostingPreferenceRequest patchCostingPreferenceRequest) {
        String url = String.format(getCisUrl(), endpointCostingPreferences);

        return (CostingPreferences) GenericRequestUtil.patch(
                RequestEntity.init(url, CostingPreferences.class)
                        .setBody(patchCostingPreferenceRequest)
                        .setStatusCode(HttpStatus.SC_OK),
                new RequestAreaApi()
        ).getResponseEntity();
    }

    public static <T>  ResponseWrapper<T> getProcessGroups() {
        String url = String.format(getCisUrl(), endpointProcessGroups);
        return GenericRequestUtil.get(
                RequestEntity.init(url, ProcessGroups.class),
                new RequestAreaApi()
        );
    }

    public static <T>  ResponseWrapper<T> getUserDefinedAttributes() {
        String url = String.format(getCisUrl(), endpointUserDefinedAttributes);
        return GenericRequestUtil.get(
                RequestEntity.init(url, UserDefinedAttributes.class),
                new RequestAreaApi()
        );
    }

    public static <T>  ResponseWrapper<T> getVirtualProductEnvironments() {
        String url = String.format(getCisUrl(), endpointVPE);
        return GenericRequestUtil.get(
                RequestEntity.init(url, VPE.class),
                new RequestAreaApi()
        );
    }

    public static <T>  ResponseWrapper<T> getDigitalFactories() {
        String url = String.format(getCisUrl(), endpointDigitalFactories);
        return GenericRequestUtil.get(
                RequestEntity.init(url, DigitalFactories.class),
                new RequestAreaApi()
        );
    }

    public static <T>  ResponseWrapper<T> getCustomAttributes() {
        String url = String.format(getCisUrl(), endpointCustomAttributes);
        return GenericRequestUtil.get(
                RequestEntity.init(url, CustomAttributes.class),
                new RequestAreaApi()
        );
    }

}
