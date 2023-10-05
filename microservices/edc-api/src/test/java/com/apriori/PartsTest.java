package com.apriori;

import static com.apriori.edc.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edc.utils.BillOfMaterialsUtil.postBillOfMaterials;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import com.apriori.edc.models.response.line.items.LineItemParts;
import com.apriori.edc.models.response.line.items.LineItemsResponse;
import com.apriori.edc.models.response.parts.Parts;
import com.apriori.edc.models.response.parts.PartsResponse;
import com.apriori.edc.utils.LineItemsUtil;
import com.apriori.edc.utils.PartsUtil;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.rules.TestRulesApi;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(TestRulesApi.class)
public class PartsTest extends PartsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;
    private SoftAssertions softAssertions = new SoftAssertions();
    private LineItemsUtil lineItems = new LineItemsUtil();
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
    @Description("GET List the line items in a bill of materials matching a specified query.")
    public void testListPartsInLineItem() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        PartsResponse allPartsInLineItem = getAllPartsInLineItem(billOfMaterialsIdentity, lineItemIdentity);
        assertThat(allPartsInLineItem.size(), is(greaterThan(0)));
    }

    @Test
    @TestRail(id = 9419)
    @Description("POST Add a new part to a line item.")
    public void testAddNewPartTOLineItem() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);

        softAssertions.assertThat(partsRequest.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        softAssertions.assertThat(partsRequest.getResponseEntity().getLineItemIdentity()).isEqualTo(lineItemIdentity);
        softAssertions.assertThat(partsRequest.getResponseEntity().getDescription()).isEqualTo("ELECTRO-TAP, 18-14 AWG RUN TAP");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 9420)
    @Description("PATCH Update a part")
    public void testUpdatePart() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);

        String partIdentity = partsRequest.getResponseEntity().getIdentity();

        ResponseWrapper<Parts> partsResponseWrapper = patchUpdatePart(billOfMaterialsIdentity, lineItemIdentity, partIdentity);

        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getIdentity()).isEqualTo(partIdentity);
        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getPinCount()).isEqualTo(5456);
        softAssertions.assertThat(partsResponseWrapper.getResponseEntity().getAverageCost()).isEqualTo(3.654);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 9421)
    @Description("POST Select a part for export")
    public void testSelectPartForExport() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        ResponseWrapper<Parts> partsRequest = postNewPartToLineItem(billOfMaterialsIdentity, lineItemIdentity);

        String partIdentity = partsRequest.getResponseEntity().getIdentity();

        postSelectPartForExport(billOfMaterialsIdentity, lineItemIdentity, partIdentity);
    }

    @Test
    @TestRail(id = 9422)
    @Description("POST Cost one or more parts in a line item.")
    public void testCostPartInLineItem() {
        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(4).getIdentity();

        List<LineItemParts> itemPartsList = new ArrayList<>(allLineItems.get(4).getParts());

        List<String> identityList = itemPartsList.stream().map(LineItemParts::getIdentity).collect(Collectors.toList());

        postSelectPartsToCost(billOfMaterialsIdentity, lineItemIdentity, identityList);
    }
}
