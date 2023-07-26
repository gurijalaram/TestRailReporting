package com.apriori.vds.tests;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.http.builder.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.testrail.TestRail;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

public class DigitalFactoriesTest extends VDSTestUtil {

    @Test
    @TestRail(id = {8034})
    @Description("POST create or updates a Digital Factory for a customer.")
    @Ignore
    public void postDigitalFactories() {
    }

    @Test
    @TestRail(id = {8030})
    @Description("Get a list of Digital Factories for a specific customer.")
    public void getDigitalFactories() {
        getDigitalFactoriesResponse();
    }

    @Test
    @TestRail(id = {8031})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getDigitalFactoriesByIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_DIGITAL_FACTORIES_BY_IDENTITY, DigitalFactory.class)
            .inlineVariables(getDigitalFactoriesResponse().getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    @Test
    @TestRail(id = {8035})
    @Description("POST create or updates a VPEs for a customer.")
    @Ignore
    public void postVPEs() {
    }

    @Test
    @TestRail(id = {8032})
    @Description("Get a list of Digital Factories for a specific customer.")
    public void getVPEs() {
        this.getVPEsResponse();
    }

    @Test
    @TestRail(id = {8033})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getVPEsByIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_VPES_BY_IDENTITY, DigitalFactory.class)
            .inlineVariables(this.getVPEsResponse().getIdentity())
            .expectedResponseCode(HttpStatus.SC_OK);

        HTTPRequest.build(requestEntity).get();
    }

    private DigitalFactory getVPEsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(VDSAPIEnum.GET_VPES, DigitalFactoriesItems.class)
            .expectedResponseCode(HttpStatus.SC_OK);

        ResponseWrapper<DigitalFactoriesItems> vpEsItemsResponse = HTTPRequest.build(requestEntity).get();

        return vpEsItemsResponse.getResponseEntity().getItems().get(0);
    }
}
