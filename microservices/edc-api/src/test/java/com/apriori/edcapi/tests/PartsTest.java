package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import com.apriori.edcapi.entity.response.line.items.LineItemParts;
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
import org.assertj.core.api.SoftAssertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PartsTest extends PartsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;
    private SoftAssertions softAssertions = new SoftAssertions();
    private LineItemsUtil lineItems = new LineItemsUtil();

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
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        PartsResponse allPartsInLineItem = getAllPartsInLineItem(billOfMaterialsIdentity, lineItemIdentity);
        assertThat(allPartsInLineItem.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(testCaseId = "9419")
    @Description("POST Add a new part to a line item.")
    public void testAddNewPartTOLineItem() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, partsRequest.getStatusCode());

        softAssertions.assertThat(partsRequest.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(partsRequest.getResponseEntity().getLineItemIdentity()).isEqualTo(lineItemIdentity);
        softAssertions.assertThat(partsRequest.getResponseEntity().getDescription()).isEqualTo("ELECTRO-TAP, 18-14 AWG RUN TAP");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "9420")
    @Description("PATCH Update a part")
    public void testUpdatePart() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);

        String partIdentity = partsRequest.getResponseEntity().getIdentity();

        ResponseWrapper<Parts> partsResponseWrapper = patchUpdatePart(billOfMaterialsIdentity, lineItemIdentity, partIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, partsResponseWrapper.getStatusCode());

        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getIdentity()).isEqualTo(partIdentity);
        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getPinCount()).isEqualTo(5456);
        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getAverageCost()).isEqualTo(3.654);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "9421")
    @Description("POST Select a part for export")
    public void testSelectPartForExport() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);

        String partIdentity = partsRequest.getResponseEntity().getIdentity();

        ResponseWrapper<Parts> partsResponseWrapper = postSelectPartForExport(billOfMaterialsIdentity, lineItemIdentity, partIdentity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, partsResponseWrapper.getStatusCode());
    }

    @Test
    @TestRail(testCaseId = "9422")
    @Description("POST Cost one or more parts in a line item.")
    public void testCostPartInLineItem() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(4).getIdentity();

        List<LineItemParts> itemPartsList = Arrays.stream(allLineItems.get(4).getParts().toArray(new LineItemParts[0])).collect(Collectors.toList());

        List<Object> identityList = Arrays.stream(itemPartsList.stream().map(LineItemParts::getIdentity).toArray()).collect(Collectors.toList());

        ResponseWrapper<Parts> partsResponseWrapper = postSelectPartsToCost(billOfMaterialsIdentity, lineItemIdentity, identityList);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, partsResponseWrapper.getStatusCode());
    }
}
