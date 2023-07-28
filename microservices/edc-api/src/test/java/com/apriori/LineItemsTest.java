package com.apriori;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.authorization.AuthorizationUtil;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.Before;
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
    @TestRail(id = 9417)
    @Description("GET List the line items in a bill of materials matching a specified query")
    public void testGetLineItems() {
        List<LineItemsResponse> allLineItems = getAllLineItems(billOfMaterialsIdentity);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(allLineItems.size()).isEqualTo(itemsCount);
        softAssertions.assertThat(allLineItems.get(1).getCustomerPartNumber()).isEqualTo(customerPartNumber);
        softAssertions.assertThat(allLineItems.get(0).getStatus()).isEqualTo(status);
        softAssertions.assertThat(allLineItems.get(0).getLevel()).isEqualTo(level);
        softAssertions.assertThat(allLineItems.get(8).getQuantity()).isEqualTo(quantity);
        softAssertions.assertThat(allLineItems.get(8).getParts().get(0).getIsUserPart()).isEqualTo(false);

        softAssertions.assertAll();
    }
}
