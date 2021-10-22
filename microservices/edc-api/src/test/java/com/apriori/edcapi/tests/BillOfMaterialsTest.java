package com.apriori.edcapi.tests;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BillOfMaterialsTest extends BillOfMaterialsUtil {

    private static BillOfMaterialsResponse testingBillOfMaterialsResponse;

    @BeforeClass
    public static void setUp() {

        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
    }

    @AfterClass
    public static void deleteTestingData() {

        if (testingBillOfMaterialsResponse != null) {
            deleteBomById(testingBillOfMaterialsResponse.getIdentity());
        }
    }

    @Test
    @TestRail(testCaseId = "1506")
    @Description("DELETE a bill of material")
    public void testDeleteBomByIdentity() {

        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(getFirstBillOfMaterials().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NOT_FOUND, HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "9413")
    @Description("POST Upload a new bill of materials")
    public void testPostBillOfMaterials() {
        String filename = "Test BOM 5.csv";
        createBillOfMaterials(filename);
    }

    @Test
    @TestRail(testCaseId = "9414")
    @Description("GET the current representation of a bill of materials")
    public void testGetBillOfMaterialsById() {

        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_BY_IDENTITY, BillOfMaterialsResponse.class)
                .inlineVariables(getFirstBillOfMaterials().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1504")
    @Description("GET List the bill of materials matching a specified query.")
    public void testGetBillOfMaterials() {
        getAllBillOfMaterials();
    }

    private static void deleteBomById(String identity) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(identity);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
        testingBillOfMaterialsResponse = null;
    }
}
