package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class LineItemsTest extends LineItemsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String identity;

    @BeforeClass
    public static void setUp() {
        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
        identity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @Test
    @TestRail(testCaseId = "9417")
    @Description("GET List the line items in a bill of materials matching a specified query")
    public void testGetLineItems() {

        SoftAssertions softAssertions = new SoftAssertions();

        List<LineItemsResponse> allLineItems = getAllLineItems(identity);

        softAssertions.assertThat(allLineItems.contains(identity)).as("IsUserPart").isEqualTo(false);
        softAssertions.assertThat(allLineItems.size()).isGreaterThan(0);
        softAssertions.assertThat(allLineItems.stream().filter(x -> x.getLineItemsPart().stream().anyMatch(y -> y.getRohs().equalsIgnoreCase("Yes"))));
        softAssertions.assertThat(allLineItems.stream().filter(x -> x.getLineItemsPart().stream().anyMatch(y -> y.getIsSaved().equals(true))));
        softAssertions.assertThat(allLineItems.stream().filter(x -> x.getQuantity().equals(1)));
        softAssertions.assertAll();
    }
}
