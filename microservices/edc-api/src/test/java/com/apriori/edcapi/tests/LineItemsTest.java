package com.apriori.edcapi.tests;

import static com.apriori.edcapi.utils.BillOfMaterialsUtil.postBillOfMaterials;

import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.response.line.items.LineItemsItemsResponse;
import com.apriori.edcapi.entity.response.line.items.LineItemsResponse;
import com.apriori.edcapi.utils.LineItemsUtil;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

public class LineItemsTest extends LineItemsUtil {

    private static String filename = "Test BOM 5.csv";
    private static String identity;

    @BeforeClass
    public static void setUp() {
        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
        identity = postBillOfMaterials(filename).getResponseEntity().getIdentity();
    }

    @Test
    public void testGetLineItems() {
        ResponseWrapper<LineItemsItemsResponse> response = getAllLineItems(identity);
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, response.getStatusCode());
    }
}
