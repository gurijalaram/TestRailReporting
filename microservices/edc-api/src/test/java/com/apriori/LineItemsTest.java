package com.apriori;

import static com.apriori.edc.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edc.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.edc.models.response.line.items.LineItemsResponse;
import com.apriori.edc.utils.LineItemsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TestRulesApi.class)
public class LineItemsTest extends LineItemsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;
    private String status = "Incomplete";
    private int level = 2;
    private String customerPartNumber = "AAA651A1";
    private int quantity = 2;
    private int itemsCount = 9;
    private UserCredentials currentUser = UserUtil.getUser();

    @AfterAll
    public static void deleteTestingData() {
        if (billOfMaterialsIdentity != null) {
            deleteBillOfMaterialById(billOfMaterialsIdentity);
        }
    }

    @BeforeEach
    public void setUp() {
        RequestEntityUtil.useTokenForRequests(currentUser.getToken());
        billOfMaterialsIdentity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
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
