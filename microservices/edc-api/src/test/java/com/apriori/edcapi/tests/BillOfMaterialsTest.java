package com.apriori.edcapi.tests;

import com.apriori.apibase.services.response.objects.BillOfMaterialsWrapper;
import com.apriori.apibase.services.response.objects.BillOfSingleMaterialWrapper;
import com.apriori.edcapi.entity.enums.EDCAPIEnum;
import com.apriori.edcapi.entity.request.bill.of.materials.BillOfMaterialsRequest;
import com.apriori.edcapi.entity.response.bill.of.materials.BillOfMaterials;
import com.apriori.edcapi.tests.util.BillOfMaterialsUtil;
import com.apriori.edcapi.tests.util.EdcTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Random;


public class BillOfMaterialsTest extends BillOfMaterialsUtil {

//    @Test
//    @Description("Get list bill of materials")
//    @Severity(SeverityLevel.NORMAL)
//    public void getBillOfMaterials() {
//        RequestEntity requestEntity = RequestEntityUtil.init(
//            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, UserUtil.getUser(), BillOfMaterialsWrapper.class);
//
//        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
//                HTTPRequest.build(requestEntity).get().getStatusCode());
//    }
//
//    @Test
//    @Description("Get list bill of materials by identity")
//    @Severity(SeverityLevel.NORMAL)
//    public void getBillOfMaterialsByIdentity() {
//        RequestEntity requestEntity = RequestEntityUtil.init(
//            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), BillOfSingleMaterialWrapper.class)
//                .inlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());
//
//        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
//                HTTPRequest.build(requestEntity).get().getStatusCode());
//    }
//
//    @Test
//    @Description("Delete list bill of materials by identity")
//    @Severity(SeverityLevel.NORMAL)
//    public void deleteBillOfMaterialsByIdentity() {
//        final int deleteIndex = new Random().nextInt(userData.getBillOfMaterials().size());
//
//        RequestEntity requestEntity = RequestEntityUtil.init(
//            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, null)
//            .inlineVariables(userData.getBillOfMaterials().get(deleteIndex).getIdentity());
//
//        userData.getBillOfMaterials().remove(deleteIndex);
//
//        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
//                HTTPRequest.build(requestEntity).delete().getStatusCode());
//    }
//
//    @Test
//    @Description("Export bill of material by identity")
//    @Severity(SeverityLevel.NORMAL)
//    public void exportBillOfMaterialsByIdentity() {
//        RequestEntity requestEntity = RequestEntityUtil.init(BillOfMaterialsAPIEnum.EXPORT_BILL_OF_MATERIALS_IDENTITY, null)
//            .inlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());
//
//        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
//            HTTPRequest.build(requestEntity).post().getStatusCode());
//    }
//
//    @Test
//    @Description("Post bill of material")
//    @Severity(SeverityLevel.NORMAL)
//    public void postBillOfMaterials() {
//        userTestDataUtil.uploadTestData(userData);
//    }

    @Test
    @TestRail(testCaseId = "1506")
    @Description("Delete a bill of material")
    public void testDeleteBomByIdentity() {

        deleteBomById(getBomIdentity());
    }

    @Test
    @TestRail(testCaseId = "9413")
    @Description("Upload a new bill of materials")
    public void testPostBillOfMaterials() {

    }

    @Test
    @TestRail(testCaseId = "9414")
    @Description("Get the current representation of a bill of materials")
    public void testGetBillOfMaterialsById() {

        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.GET_BILL_OF_MATERIALS_BY_IDENTITY, BillOfMaterials.class)
                .inlineVariables(getFirstBillOfMaterials().getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK, HTTPRequest.build(requestEntity).get().getStatusCode());

    }

    @Test
    public void testGetBillOfMaterials() {

    }

    private static BillOfMaterials createBillOfMaterials(final BillOfMaterialsRequest billOfMaterialsRequest) {
        RequestEntity requestEntity =
            RequestEntityUtil.init(EDCAPIEnum.POST_BILL_OF_MATERIALS, BillOfMaterials.class)
                .body(billOfMaterialsRequest);

        ResponseWrapper<BillOfMaterials> createBillOfMaterialsResponse = HTTPRequest.build(requestEntity).post();
        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, createBillOfMaterialsResponse.getStatusCode());

        return createBillOfMaterialsResponse.getResponseEntity();
    }

    private static BillOfMaterialsRequest convertBillOfMaterialsToRequest(BillOfMaterials backupBillOfMaterials) {
        return BillOfMaterialsRequest
            .builder()
            .createdBy(backupBillOfMaterials.getCreatedBy())
            .identity(backupBillOfMaterials.getIdentity())
            .filename(backupBillOfMaterials.getFilename())
            .name(backupBillOfMaterials.getName())
            .build();
    }
}
