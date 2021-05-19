package com.apriori.vds.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;
import com.apriori.vds.entity.enums.VDSAPIEnum;
import com.apriori.vds.entity.response.digital.factories.DigitalFactoriesItems;
import com.apriori.vds.entity.response.digital.factories.DigitalFactory;
import com.apriori.vds.entity.response.digital.factories.VPE;
import com.apriori.vds.entity.response.digital.factories.VPEsItems;
import com.apriori.vds.tests.util.VDSRequestEntityUtil;
import com.apriori.vds.tests.util.VDSTestUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class DigitalFactoriesTest extends VDSTestUtil {

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
            .inlineVariables(Collections.singletonList(this.getDigitalFactoriesResponse().getIdentity()));

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTP2Request.build(requestEntity).get().getStatusCode()
        );
    }

    private DigitalFactory getDigitalFactoriesResponse() {
        RequestEntity requestEntity = VDSRequestEntityUtil.initWithSharedSecret(VDSAPIEnum.GET_DIGITAL_FACTORIES, DigitalFactoriesItems.class);

        ResponseWrapper<DigitalFactoriesItems> digitalFactoriesItemsResponse = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            digitalFactoriesItemsResponse.getStatusCode()
        );

        return digitalFactoriesItemsResponse.getResponseEntity().getItems().get(0);
    }



    @Test
    @TestRail(testCaseId = {"8035"})
    @Description("POST create or updates a VPEs for a customer.")
    public void postVPEs() {

        RequestEntity requestEntity = VDSRequestEntityUtil.init(VDSAPIEnum.POST_VPES, null)
//            .headers(new HashMap<String, String>() {{
//                put("Content-Type", "multipart/form-data");
//            }})
            .formParams(new FormParams())
            .customBody("{\n" +
                "   \"subjectIdentity\":\"8GFDIG229629\",\n" +
                "   \"permissions\":[\n" +
                "      \"READ\",\n" +
                "      \"COST_USING\",\n" +
                "      \"DELETE\",\n" +
                "      \"UPDATE\"\n" +
                "   ],\n" +
                "   \"active\":true,\n" +
                "   \"annualVolume\":5500,\n" +
                "   \"batchesPerYear\":12.0,\n" +
                "   \"description\":\"Data Release: 20201201\\nSubversion Revision: 98969\",\n" +
                "   \"location\":\"Brazil\",\n" +
                "   \"name\":\"aPriori Brazil\",\n" +
                "   \"ownerType\":\"PRIVATE\",\n" +
                "   \"productionLife\":5.0,\n" +
                "   \"revision\":\"aP2021R1_SP00_F00_(2021-04)\",\n" +
                "   \"useType\":\"COSTING\",\n" +
                "   \"vpeType\":\"aPriori - Regional Data Libraries - Standard\",\n" +
                "   \"processGroupAssociations\":{\n" +
                "      \"Composites\":{\n" +
                "         \"processGroupIdentity\":\"E3DC2E44D494\"\n" +
                "      }\n" +
                "   }\n" +
                "}");

        HTTP2Request.build(requestEntity).post();

        //{
        //   "identity":"9H3L9AILDG11",
        //   "createdBy":"#ETL00000000",
        //   "createdAt":"2021-05-17T18:26Z",
        //   "subjectIdentity":"8GFDIG229629",
        //   "permissions":[
        //      "READ",
        //      "COST_USING",
        //      "DELETE",
        //      "UPDATE"
        //   ],
        //   "customerIdentity":"H337GKD0LA0M",
        //   "active":true,
        //   "annualVolume":5500,
        //   "batchesPerYear":12.0,
        //   "description":"Data Release: 20201201\nSubversion Revision: 98969",
        //   "location":"Brazil",
        //   "name":"aPriori Brazil",
        //   "ownerType":"PRIVATE",
        //   "productionLife":5.0,
        //   "revision":"aP2021R1_SP00_F00_(2021-04)",
        //   "useType":"COSTING",
        //   "vpeType":"aPriori - Regional Data Libraries - Standard",
        //   "processGroupAssociations":{
        //      "Composites":{
        //         "processGroupIdentity":"E3DC2E44D494"
        //      }
        //   }
        //}
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
