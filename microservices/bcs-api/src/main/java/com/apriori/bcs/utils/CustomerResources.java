package com.apriori.bcs.utils;

import com.apriori.apibase.services.bcs.objects.CisCustomers;
import com.apriori.apibase.services.bcs.objects.CostingPreferences;
import com.apriori.apibase.services.bcs.objects.ProcessGroups;
import com.apriori.apibase.services.bcs.objects.UserDefinedAttributes;
import com.apriori.apibase.services.bcs.objects.VPE;
import com.apriori.apibase.services.bcs.objects.requests.PatchCostingPreferenceRequest;
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

    public static <T>  ResponseWrapper<T> getCustomers() {
        String url = String.format(getBaseCisUrl(), "");
        return GenericRequestUtil.get(
                RequestEntity.init(url, CisCustomers.class),
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

}
