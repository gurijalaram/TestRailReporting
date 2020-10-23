package com.apriori.edc.tests;

import com.apriori.apibase.services.response.objects.BillOfMaterialsWrapper;
import com.apriori.apibase.services.response.objects.BillOfSingleMaterialWrapper;
import com.apriori.edc.tests.util.EdcTestUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.enums.common.api.BillOfMaterialsAPIEnum;
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
        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, UserUtil.getUser(), BillOfMaterialsWrapper.class)
                .setToken(token)
                .setAutoLogin(true);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getStatusCode());
    }

    @Test
    @Description("Get list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void getBillOfMaterialsByIdentity() {
        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), BillOfSingleMaterialWrapper.class)
                .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity())
                .setToken(token)
                .setAutoLogin(true);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getStatusCode());
    }

    @Test
    @Description("Delete list bill of materials by identity")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBillOfMaterialsByIdentity() {
        final int deleteIndex = new Random().nextInt(userData.getBillOfMaterials().size());

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(userData.getBillOfMaterials().get(deleteIndex).getIdentity())
                .setToken(token)
                .setAutoLogin(true);

        userData.getBillOfMaterials().remove(deleteIndex);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
                GenericRequestUtil.delete(requestEntity, new RequestAreaApi()).getStatusCode());


    }

    @Test
    @Description("Export bill of material by identity")
    @Severity(SeverityLevel.NORMAL)
    public void exportBillOfMaterialsByIdentity() {

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.EXPORT_BILL_OF_MATERIALS_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(userData.getBillOfMaterials().get(new Random().nextInt(userData.getBillOfMaterials().size())).getIdentity())
                .setToken(token)
                .setAutoLogin(true);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getStatusCode());
    }

    @Test
    @Description("Post bill of material")
    @Severity(SeverityLevel.NORMAL)
    public void postBillOfMaterials() {
        userTestDataUtil.uploadTestData(userData);
    }
}
