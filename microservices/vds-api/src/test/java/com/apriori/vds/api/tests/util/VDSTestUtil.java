package com.apriori.vds.api.tests.util;

import com.apriori.shared.util.SharedCustomerUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.request.HTTPRequest;
import com.apriori.shared.util.http.utils.QueryParams;
import com.apriori.shared.util.http.utils.RequestEntityUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.http.utils.TestUtil;
import com.apriori.vds.api.enums.VDSAPIEnum;
import com.apriori.vds.api.models.response.access.control.AccessControlGroup;
import com.apriori.vds.api.models.response.access.control.AccessControlGroupItems;
import com.apriori.vds.api.models.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.api.models.response.digital.factories.DigitalFactory;

import org.apache.http.HttpStatus;

import java.util.List;

public class VDSTestUtil extends TestUtil {
    public final String customerId = SharedCustomerUtil.getCurrentCustomerIdentity();
    private RequestEntityUtil requestEntityUtil;

    public VDSTestUtil(RequestEntityUtil requestEntityUtil) {
        super.requestEntityUtil = requestEntityUtil;
        this.requestEntityUtil = requestEntityUtil;
    }

    /**
     * Calls an API with GET verb
     *
     * @return new object
     */
    public List<DigitalFactory> getDigitalFactories() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.DIGITAL_FACTORIES, DigitalFactoriesItems.class)
            .queryParams(new QueryParams().use("pageSize", "100").use("name[EQ]", "aPriori USA"))
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponseWrapper = HTTPRequest.build(requestEntity).get();

        return digitalFactoriesItemsResponseWrapper.getResponseEntity().getItems();
    }

    /**
     * Calls an API with GET verb
     *
     * @return new object
     */
    public DigitalFactory getFirstDigitalFactory() {
        return getDigitalFactories().stream().findFirst().get();
    }

    /**
     * Calls an API with GET verb
     *
     * @return new object
     */
    public DigitalFactory getDigitalFactoryById() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.DIGITAL_FACTORIES_BY_IDENTITY, DigitalFactory.class)
            .inlineVariables(getFirstDigitalFactory().getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactory> digitalFactoriesItemsResponseWrapper = HTTPRequest.build(requestEntity).get();

        return digitalFactoriesItemsResponseWrapper.getResponseEntity();
    }

    /**
     * Calls an API with GET verb
     *
     * @return new object
     */
    public List<AccessControlGroup> getAccessControlGroups() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.GROUPS, AccessControlGroupItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<AccessControlGroupItems> accessControlGroupsResponse = HTTPRequest.build(requestEntity).get();

        return accessControlGroupsResponse.getResponseEntity().getItems();
    }

    /**
     * Calls an API with GET verb
     * @return new object
     */
    public List<DigitalFactory> getVpes() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.VPES, DigitalFactoriesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactoriesItems> vpEsItemsResponse = HTTPRequest.build(requestEntity).get();

        return vpEsItemsResponse.getResponseEntity().getItems();
    }

    /**
     * Calls an API with GET verb
     *
     * @return new object
     */
    public DigitalFactory getVpeById() {
        RequestEntity requestEntity = requestEntityUtil.init(VDSAPIEnum.VPES_BY_IDENTITY, DigitalFactory.class)
            .inlineVariables(getVpes().stream().findFirst().get().getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactory> response = HTTPRequest.build(requestEntity).get();

        return response.getResponseEntity();
    }
}
