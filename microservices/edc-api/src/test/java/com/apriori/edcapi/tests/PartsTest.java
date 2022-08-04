package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.deleteBillOfMaterialById;
import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.edcapi.utils.PartsUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.authorization.AuthorizationUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;

import io.qameta.allure.Description;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class PartsTest extends PartsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String billOfMaterialsIdentity;
    private static LineItemsUtil lineItemsPart;


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
    @TestRail(testCaseId = "")
    @Description("")
    public void testListPartsInLineItem() {
        LineItemsUtil lineItems = new LineItemsUtil();

        List<LineItemsResponse> allLineItems = lineItems.getAllLineItems(billOfMaterialsIdentity);

        String lineItemIdentity = allLineItems.get(0).getIdentity();

        getAllPartsInLineItem(billOfMaterialsIdentity, lineItemIdentity);

//        RequestEntity requestEntity =
//            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_LINE_ITEMS_PARTS, PartsItemsResponse.class)
//                .inlineVariables(billOfMaterialsIdentity, LineItemsUtil.getAllLineItems(billOfMaterialsIdentity).toString());
//
//        ResponseWrapper<PartsItemsResponse> getAllPartsResponse = HTTPRequest.build(requestEntity).get();
//        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, getAllPartsResponse.getStatusCode());
    }
}
