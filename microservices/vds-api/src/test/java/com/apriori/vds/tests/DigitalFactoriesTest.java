package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.digital.factories.VPE;
import com.apriori.vds.entity.response.digital.factories.VPEsItems;
import com.apriori.vds.tests.util.DigitalAndPGMUtil;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

public class DigitalFactoriesTest extends DigitalAndPGMUtil {

    @Test
    @TestRail(testCaseId = {"8034"})
    @Description("POST create or updates a Digital Factory for a customer.")
    @Ignore
    public void postDigitalFactories() {
    }

    @Test
    @TestRail(testCaseId = {"8030"})
    @Description("Get a list of Digital Factories for a specific customer.")
    public void getDigitalFactories() {
        this.getDigitalFactoriesResponse();
    }

    @Test
    @TestRail(testCaseId = {"8031"})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getDigitalFactoriesByIdentity() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_DIGITAL_FACTORIES_BY_IDENTITY, DigitalFactory.class)
            .inlineVariables(Collections.singletonList(getDigitalFactoriesResponse().getIdentity()));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    @Test
    @TestRail(testCaseId = {"8035"})
    @Description("POST create or updates a VPEs for a customer.")
    @Ignore
    public void postVPEs() {
    }

    @Test
    @TestRail(testCaseId = {"8032"})
    @Description("Returns a list of Digital Factories for a specific customer.")
    public void getVPEs() {
        this.getVPEsResponse();
    }

    @Test
    @TestRail(testCaseId = {"8033"})
    @Description("Get a specific Digital Factory for a customer identified by its identity.")
    public void getVPEsByIdentity() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_VPES_BY_IDENTITY, VPE.class)
            .inlineVariables(Collections.singletonList(this.getVPEsResponse().getIdentity()));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    private VPE getVPEsResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_VPES, VPEsItems.class);

        ResponseWrapper<VPEsItems> vpEsItemsResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            vpEsItemsResponse.getStatusCode()
        );

        return vpEsItemsResponse.getResponseEntity().getItems().get(0);
    }


}
