package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.token.TokenUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LineItemsTest extends LineItemsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;
    private String status = "Incomplete";
    private int level = 2;
    private String customerPartNumber = "AAA651A1";
    private int quantity = 2;
    private int itemsCount = 9;

    @BeforeClass
    public static void setUp() {
        RequestEntityUtil.useTokenForRequests(new TokenUtil().getTokenAsString());
        billOfMaterialsIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @AfterClass
    public static void deleteTestingData() {
        if (billOfMaterialsIdentity != null) {
            deleteBillOfMaterialById(billOfMaterialsIdentity);
        }
    }

    @Test
    @TestRail(testCaseId = "9417")
    @Description("GET List the line items in a bill of materials matching a specified query")
    public void testGetLineItems() {

        SoftAssertions softAssertions = new SoftAssertions();

        List<LineItemsResponse> allLineItems = getAllLineItems(billOfMaterialsIdentity);

        softAssertions.assertThat(allLineItems.size()).isEqualTo(itemsCount);
        softAssertions.assertThat(allLineItems.get(1).getCustomerPartNumber()).isEqualTo(customerPartNumber);
        softAssertions.assertThat(allLineItems.get(0).getStatus()).isEqualTo(status);
        softAssertions.assertThat(allLineItems.get(0).getLevel()).isEqualTo(level);
        softAssertions.assertThat(allLineItems.get(8).getQuantity()).isEqualTo(quantity);
        softAssertions.assertThat(allLineItems.get(8).getLineItemsPart().get(0).getIsUserPart()).isEqualTo(false);
        softAssertions.assertAll();
    }
}
