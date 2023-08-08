package com.apriori;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.edc.models.response.bill.of.materials.BillOfMaterialsResponse;
import com.apriori.edc.utils.BillOfMaterialsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.models.AuthorizationUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BillOfMaterialsTest extends BillOfMaterialsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;

    @AfterAll
    public static void deleteTestingData() {
        if (billOfMaterialsIdentity != null) {
            deleteBillOfMaterialById(billOfMaterialsIdentity);
        }
    }

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(new AuthorizationUtil().getTokenAsString());
        billOfMaterialsIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @Test
    @TestRail(id = 9415)
    @Description("DELETE a bill of materials")
    public void testDeleteBomByIdentity() {
        String postResponseIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();

        getBillOfMaterialById(postResponseIdentity);

        deleteBillOfMaterialById(postResponseIdentity);

        getBillOfMaterialById(postResponseIdentity, null, HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @TestRail(id = 9413)
    @Description("POST Upload a new bill of materials")
    public void testPostBillOfMaterials() {
        getBillOfMaterialById(postBillOfMaterials(filename).getResponseEntity().getIdentity());
    }

    @Test
    @TestRail(id = 9414)
    @Description("GET the current representation of a bill of materials")
    public void testGetBillOfMaterialsById() {
        getBillOfMaterialById(billOfMaterialsIdentity);
    }

    @Test
    @TestRail(id = 1504)
    @Description("GET List of all bill of materials.")
    public void testGetAllBillOfMaterials() {
        List<BillOfMaterialsResponse> billOfMaterialsItems = getAllBillOfMaterials();
        assertThat(billOfMaterialsItems.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(id = 9416)
    @Description("POST Export a bill of materials as a CSV file.")
    public void testExportBomAsCsvFile() {
        postExportBomAsCsvFile(billOfMaterialsIdentity);
    }
}
