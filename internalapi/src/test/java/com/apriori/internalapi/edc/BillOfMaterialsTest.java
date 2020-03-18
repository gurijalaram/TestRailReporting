package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.response.common.BillOfMaterialsWrapper;
import com.apriori.apibase.http.builder.common.response.common.BillOfSingleMaterialWrapper;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.builder.service.RequestAreaUiAuth;
import com.apriori.apibase.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.internalapi.edc.util.UserDataEDC;
import com.apriori.internalapi.edc.util.UserTestDataUtil;

import com.apriori.internalapi.util.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;


public class BillOfMaterialsTest extends TestUtil {

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
        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userData.getUserCredentials(), BillOfMaterialsWrapper.class);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth()).getStatusCode());
    }

    @Test
    @Description("Get list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterialsByIdentity() {
        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), BillOfSingleMaterialWrapper.class)
                .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth()).getStatusCode());
    }

    @Test
    @Description("Delete list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBillOfMaterialsByIdentity() {
        final int deleteIndex = new Random().nextInt(userData.getBillOfMaterials().size());

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(userData.getBillOfMaterials().get(deleteIndex).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
                GenericRequestUtil.delete(requestEntity, new RequestAreaUiAuth()).getStatusCode());

        userData.getBillOfMaterials().remove(deleteIndex);
    }

    @Test
    @Description("Export bill of material by identity")
    @Severity(SeverityLevel.NORMAL)
    public void exportBillOfMaterialsByIdentity() {

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.EXPORT_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth()).getStatusCode());
    }

    @Test
    @Description("Post bill of material")
    @Severity(SeverityLevel.NORMAL)
    public void postBillOfMaterials() {
        userTestDataUtil.uploadTestData(userData);
    }
}
