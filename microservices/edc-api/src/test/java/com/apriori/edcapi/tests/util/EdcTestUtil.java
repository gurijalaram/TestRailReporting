package com.apriori.edcapi.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsItems;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsResponse;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.List;

public class EdcTestUtil extends TestUtil {

    protected static UserDataEDC userData;

    @BeforeClass
    public static void setUp() {

        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
    }

    protected static BillOfMaterialsResponse getBillOfMaterialsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItems.class);
        ResponseWrapper<BillOfMaterialsItems> billOfMaterialsItemsResponseWrapper = HTTPRequest.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            billOfMaterialsItemsResponseWrapper.getStatusCode());

        List<BillOfMaterialsResponse> billOfMaterials = billOfMaterialsItemsResponseWrapper.getResponseEntity().getItems();
        Assert.assertNotEquals("To get Bill of Materials, response should contain it.", 0, billOfMaterials.size());

        return billOfMaterialsItemsResponseWrapper.getResponseEntity().getItems().get(0);
    }
}
