package com.apriori.edc.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edc.entity.enums.EDCAPIEnum;
import com.apriori.edc.entity.response.bill.of.materials.BillOfMaterials;
import com.apriori.edc.entity.response.bill.of.materials.BillOfMaterialsItems;
import com.apriori.edc.tests.BillOfMaterialsTest;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.service.HTTP2Request;
import com.apriori.utils.http2.utils.RequestEntityUtil;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EdcTestUtil extends TestUtil {

    protected static UserTestDataUtil userTestDataUtil;
    protected static UserDataEDC userData;
    private static BillOfMaterials billOfMaterials;
    private static String billOfMaterialsIdentity;

    protected static final Set<String> billOfMaterialsIdsToDelete = new HashSet<>();

    @BeforeClass
    public static void setUp() {

        RequestEntityUtil.useTokenForRequests(new JwtTokenUtil().retrieveJwtToken());
    }

    @AfterClass
    public static void deleteTestingData() {
        billOfMaterialsIdsToDelete.forEach(BillOfMaterialsTest::deleteBomById);
    }

    public static void deleteBomById(final String identity) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(getBomIdentity());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTP2Request.build(requestEntity).delete().getStatusCode());
    }

    protected static BillOfMaterials getBillOfMaterialsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItems.class);
        ResponseWrapper<BillOfMaterialsItems> billOfMaterialsItemsResponseWrapper = HTTP2Request.build(requestEntity).get();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            billOfMaterialsItemsResponseWrapper.getStatusCode());

        List<BillOfMaterials> billOfMaterials = billOfMaterialsItemsResponseWrapper.getResponseEntity().getItems();
        Assert.assertNotEquals("To get Bill of Materials, response should contain it.", 0, billOfMaterials.size());

         return billOfMaterialsItemsResponseWrapper.getResponseEntity().getItems().get(0);
    }

    public static BillOfMaterials getBoms() {
        if (billOfMaterials == null) {
            billOfMaterials = getBillOfMaterialsResponse();
        }
        return billOfMaterials;
    }

    public static String getBomIdentity() {
        if (billOfMaterialsIdentity == null) {
            billOfMaterialsIdentity = getBoms().getIdentity();
        }
        return billOfMaterialsIdentity;
    }
}
