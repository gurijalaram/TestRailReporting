package com.apriori.edcapi.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BillOfMaterialsTest extends BillOfMaterialsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;

    @Before
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        billOfMaterialsIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void deleteTestingData() {
        if (billOfMaterialsIdentity != null) {
            deleteBillOfMaterialById(billOfMaterialsIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = "9415")
    @Description("DELETE a bill of materials")
    public void testDeleteBomByIdentity() {
        String postResponseIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();

        getBillOfMaterialById(postResponseIdentity);

        deleteBillOfMaterialById(postResponseIdentity);

        getBillOfMaterialById(postResponseIdentity, null, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(testCaseId = "9413")
    @Description("POST Upload a new bill of materials")
    public void testPostBillOfMaterials() {
        getBillOfMaterialById(postBillOfMaterials(filename).getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(testCaseId = "9414")
    @Description("GET the current representation of a bill of materials")
    public void testGetBillOfMaterialsById() {
        getBillOfMaterialById(billOfMaterialsIdentity);
    }

    @Test
    @TestRail(testCaseId = "1504")
    @Description("GET List of all bill of materials.")
    public void testGetAllBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialsItems = getAllBillOfMaterials();
        assertThat(billOfMaterialsItems.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "9416")
    @Description("POST Export a bill of materials as a CSV file.")
    public void testExportBomAsCsvFile() {
        postExportBomAsCsvFile(billOfMaterialsIdentity);
    }
}
