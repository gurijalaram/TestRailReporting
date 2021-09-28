package com.apriori.edcapi.tests;

import com.apriori.apibase.services.response.objects.BillOfMaterialsWrapper;
import com.apriori.apibase.services.response.objects.BillOfSingleMaterialWrapper;
import com.apriori.edcapi.tests.util.EdcTestUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.Random;


public class BillOfMaterialsTest extends EdcTestUtil {

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterials() {
        RequestEntity requestEntity = RequestEntityUtil.init(
            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, UserUtil.getUser(), BillOfMaterialsWrapper.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @Description("Get list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterialsByIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(
            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), BillOfSingleMaterialWrapper.class)
                .inlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @Description("Delete list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBillOfMaterialsByIdentity() {
        final int deleteIndex = new Random().nextInt(userData.getBillOfMaterials().size());

        RequestEntity requestEntity = RequestEntityUtil.init(
            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, null)
            .inlineVariables(userData.getBillOfMaterials().get(deleteIndex).getIdentity());

        userData.getBillOfMaterials().remove(deleteIndex);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
                HTTPRequest.build(requestEntity).delete().getStatusCode());
    }

    @Test
    @Description("Export bill of material by identity")
    @Severity(SeverityLevel.NORMAL)
    public void exportBillOfMaterialsByIdentity() {
        RequestEntity requestEntity = RequestEntityUtil.init(BillOfMaterialsAPIEnum.EXPORT_BILL_OF_MATERIALS_IDENTITY, null)
            .inlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).post().getStatusCode());
    }

    @Test
    @Description("Post bill of material")
    @Severity(SeverityLevel.NORMAL)
    public void postBillOfMaterials() {
        userTestDataUtil.uploadTestData(userData);
    }

    @Test
    @TestRail(testCaseId = "1506")
    @Description("Delete a bill of material")
    public void testDeleteBomByIdentity() {

        deleteBomById(getBomIdentity());
    }
}
