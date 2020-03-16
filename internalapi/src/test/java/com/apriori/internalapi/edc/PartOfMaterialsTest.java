package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.response.common.MaterialPart;
import com.apriori.apibase.http.builder.common.response.common.MaterialPartWrapper;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.service.RequestAreaUiAuth;
import com.apriori.apibase.http.enums.common.api.PartsAPIEnum;
import com.apriori.apibase.utils.ResponseWrapper;
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

import java.util.List;
import java.util.stream.Collectors;

public class PartOfMaterialsTest extends TestUtil {

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
    @Description("Get list of parts")
    @Severity(SeverityLevel.NORMAL)
    public void getParts() {
        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.GET_PARTS_BY_BILL_AND_LINE_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity()
                );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth()).getStatusCode());
    }

    @Test
    @Description("Post the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void postPart() {
        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(userData.getMaterialPart());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
                GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth()).getStatusCode());
    }

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void costPart() {
        final List<String> costingIdentity = userData.getLineItem().getMaterialParts().stream()
            .map(MaterialPart::getIdentity)
            .collect(Collectors.toList());

        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(costingIdentity);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth()).getStatusCode());


//        new HTTPRequest().unauthorized()
//            .customizeRequest()
//            .setEndpoint(PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY)
//            .setHeaders(userData.getAuthorizationHeaders())
//            .setInlineVariables(
//                userData.getBillOfMaterial().getIdentity(),
//                userData.getLineItem().getIdentity())
//            .setBody(costingIdentity)
//            .commitChanges()
//            .connect()
//            .post();
    }

    @Test
    @Description("Patch the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void updatePart() {
        doUpdatePart();
    }

    private MaterialPart doUpdatePart() {
        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY, userData.getUserCredentials(), MaterialPartWrapper.class)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(userData.getMaterialPart().setAverageCost(5f));

        final ResponseWrapper<MaterialPartWrapper> materialPartWrapper = GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, materialPartWrapper.getStatusCode());

        return materialPartWrapper.getResponseEntity().getMaterialPart();
    }

    @Test
    @Description("Get the part from environment")
    @Severity(SeverityLevel.NORMAL)
    public void selectPart() {

        MaterialPart materialPart = doUpdatePart();

        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        materialPart.getLineItemIdentity(),
                        materialPart.getIdentity())
                .setBody(userData.getMaterialPart().setAverageCost(5f));

        ResponseWrapper<MaterialPartWrapper> materialPartWrapper = GenericRequestUtil.post(requestEntity, new RequestAreaUiAuth());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, materialPartWrapper.getStatusCode());

//        new HTTPRequest().unauthorized()
//            .customizeRequest()
//            .setEndpoint(PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY)
//            .setStatusCode(204)
//            .setHeaders(userData.getAuthorizationHeaders())
//            .setInlineVariables(
//                userData.getBillOfMaterial().getIdentity(),
//                materialPart.getLineItemIdentity(),
//                materialPart.getIdentity())
//            .commitChanges()
//            .connect()
//            .post();
    }
}
