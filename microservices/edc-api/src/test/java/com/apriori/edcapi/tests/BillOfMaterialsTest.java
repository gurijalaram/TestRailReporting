package com.apriori.edcapi.tests;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BillOfMaterialsTest extends BillOfMaterialsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String identity;

    @BeforeClass
    public static void setUp() {

        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
        identity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void deleteTestingData() {

        if (identity != null) {
            deleteBillOfMaterialById(identity);
        }
    }

    @Test
    @TestRail(testCaseId = "9415")
    @Description("DELETE a bill of material")
    public void testDeleteBomByIdentity() {
        ResponseWrapper<BillOfMaterialsResponse> postResponse = postBillOfMaterials(filename);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, postResponse.getStatusCode());
        String postResponseIdentity = postResponse.getResponseEntity().getIdentity();

        ResponseWrapper<BillOfMaterialsResponse> getResponse = getBillOfMaterialById(postResponseIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getResponse.getStatusCode());

        ResponseWrapper<BillOfMaterialsResponse> deleteResponse = deleteBillOfMaterialById(postResponseIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, deleteResponse.getStatusCode());

        ResponseWrapper<BillOfMaterialsResponse> getResponseDeleted = getBillOfMaterialById(postResponseIdentity, null);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NOT_FOUND, getResponseDeleted.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "9413")
    @Description("POST Upload a new bill of materials")
    public void testPostBillOfMaterials() {
        ResponseWrapper<BillOfMaterialsResponse> postResponse = postBillOfMaterials(filename);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, postResponse.getStatusCode());

        ResponseWrapper<BillOfMaterialsResponse> getResponse = getBillOfMaterialById(postResponse.getResponseEntity().getIdentity());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getResponse.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "9414")
    @Description("GET the current representation of a bill of materials")
    public void testGetBillOfMaterialsById() {

        ResponseWrapper<BillOfMaterialsResponse> getResponse = getBillOfMaterialById(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getResponse.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "1504")
    @Description("GET List the bill of materials matching a specified query.")
    public void testGetAllBillOfMaterials() {
        getAllBillOfMaterials();
    }
}
