package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

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
        assertThat(allLineItems.get(0).getStatus(), is("Incomplete"));
        assertThat(allLineItems.get(1).getQuantity(), is(1));

        softAssertions.assertThat(allLineItems.contains(identity));
        softAssertions.assertThat(allLineItems.get(8).getLineItemsPart().get(1).getRohsVersion());
        softAssertions.assertThat(allLineItems.size());
        softAssertions.assertAll();
    }
}
