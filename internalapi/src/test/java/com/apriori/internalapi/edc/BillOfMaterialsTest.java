package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.response.common.BillOfMaterialsWrapper;
import com.apriori.apibase.http.builder.common.response.common.BillOfSingleMaterialWrapper;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.internalapi.edc.util.UserDataEDC;
import com.apriori.internalapi.edc.util.UserTestDataUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;


public class BillOfMaterialsTest {

    private static UserTestDataUtil userTestDataUtil;
    private static UserDataEDC userData;

    @BeforeClass
    public static void setUp() {
        userTestDataUtil = new UserTestDataUtil();
        userData = userTestDataUtil.initBillOfMaterials();
    }

    @AfterClass
    public static void clearTestData() {
        userTestDataUtil.clearTestData(userData);
    }

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterials() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS)
            .setReturnType(BillOfMaterialsWrapper.class)
            .commitChanges()
            .connect()
            .get();
    }

    @Test
    @Description("Get list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterialsByIdentity() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY)
            .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity())
            .setReturnType(BillOfSingleMaterialWrapper.class)
            .commitChanges()
            .connect()
            .get();

    }

    @Test
    @Description("Delete list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBillOfMaterialsByIdentity() {
        final int deleteIndex = new Random().nextInt(userData.getBillOfMaterials().size());

        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY)
            .setInlineVariables(userData.getBillOfMaterials().get(deleteIndex).getIdentity())
            .setStatusCode(204)
            .commitChanges()
            .connect()
            .delete();

        userData.getBillOfMaterials().remove(deleteIndex);
    }

    @Test
    @Description("Export bill of material by identity")
    @Severity(SeverityLevel.NORMAL)
    public void exportBillOfMaterialsByIdentity() {
        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userData.getAuthorizationHeaders())
            .setEndpoint(BillOfMaterialsAPIEnum.EXPORT_BILL_OF_MATERIALS_IDENTITY)
            .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity())
            .commitChanges()
            .connect()
            .post();
    }

    @Test
    @Description("Post bill of material")
    @Severity(SeverityLevel.NORMAL)
    public void postBillOfMaterials() {
        userTestDataUtil.uploadTestData(userData);
    }
}
