package com.apriori.edcapi.tests.util;

import com.apriori.apibase.utils.TestUtil;
import com.apriori.ats.utils.JwtTokenUtil;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterials;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterialsItems;
import com.apriori.edcapi.tests.BillOfMaterialsTest;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillOfMaterialsUtil extends TestUtil {

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

    protected static List<BillOfMaterials> getBillOfMaterials() {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsItems.class);

        ResponseWrapper <BillOfMaterialsItems> responseWrapper = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, responseWrapper.getStatusCode());

        return responseWrapper.getResponseEntity()
            .getItems();
    }

    public static void deleteBomById(final String identity) {
        final RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.DELETE_BILL_OF_MATERIALS_BY_IDENTITY, null)
                .inlineVariables(getBomIdentity());
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, HTTPRequest.build(requestEntity).delete().getStatusCode());
    }

    protected static BillOfMaterials getFirstBillOfMaterials() {
        List<BillOfMaterials> billOfMaterials =  getBillOfMaterials();
        Assert.assertNotEquals("To get bill of materials it should present.", billOfMaterials.size(), 0);

        return billOfMaterials.get(0);
    }

    protected static List<BillOfMaterials> getBillOfMaterialsResponse() {
        RequestEntity requestEntity = RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterials.class);

        ResponseWrapper<BillOfMaterialsItems> billOfMaterialsResponse = HTTPRequest.build(requestEntity).get();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, billOfMaterialsResponse.getStatusCode());

        return billOfMaterialsResponse.getResponseEntity().getItems();
    }

    public static BillOfMaterials getBoms() {
        if (billOfMaterials == null) {
            billOfMaterials = (BillOfMaterials) getBillOfMaterialsResponse();
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
