package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.edcapi.entity.request.PartsRequest;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.entity.response.parts.Parts;
import com.apriori.edcapi.entity.response.parts.PartsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.edcapi.utils.PartsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PartsTest extends PartsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;

    @BeforeClass
    public static void setUp() {
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
    @TestRail(testCaseId = "9417")
    @Description("GET List the line items in a bill of materials matching a specified query.")
    public void testListPartsInLineItem() {
        LineItemsUtil lineItems = new LineItemsUtil();

        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        PartsResponse allPartsInLineItem = getAllPartsInLineItem(billOfMaterialsIdentity, lineItemIdentity);
        assertThat(allPartsInLineItem.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "9419")
    @Description("POST Add a new part to a line item.")
    public void testAddNewPartTOLineItem() {
        LineItemsUtil lineItems = new LineItemsUtil();

        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, partsRequest.getStatusCode());
    }
}
