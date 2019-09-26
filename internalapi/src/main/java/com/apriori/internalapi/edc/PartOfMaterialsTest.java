package com.apriori.internalapi.edc;

import com.apriori.apibase.http.builder.common.response.common.MaterialPart;
import com.apriori.apibase.http.builder.common.response.common.MaterialPartWrapper;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.enums.common.api.PartsAPIEnum;
import com.apriori.internalapi.edc.util.UserDataEDC;
import com.apriori.internalapi.edc.util.UserTestDataUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PartOfMaterialsTest {

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
        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.GET_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setHeaders(userData.getAuthorizationHeaders())
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .commitChanges()
                .connect()
                .get();
    }

    @Test
    @Description("Post the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void postPart() {

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setHeaders(userData.getAuthorizationHeaders())
                .setEndpoint(PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setReturnType(MaterialPartWrapper.class)
                .setStatusCode(201)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(userData.getMaterialPart())
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void costPart() {

        List<String> costingIdentity = userData.getLineItem().getMaterialParts().stream()
                .map(MaterialPart::getIdentity)
                .collect(Collectors.toList());

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setHeaders(userData.getAuthorizationHeaders())
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(costingIdentity)
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Patch the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void updatePart() {
        doUpdatePart();
    }

    private MaterialPart doUpdatePart() {
        MaterialPartWrapper materialPartWrapper =  (MaterialPartWrapper) new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setReturnType(MaterialPartWrapper.class)
                .setHeaders(userData.getAuthorizationHeaders())
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity())
                .setBody(userData.getMaterialPart().setAverageCost(5f))
                .setStatusCode(201)
                .commitChanges()
                .connect()
                .post();
        return materialPartWrapper.getMaterialPart();
    }

    @Test
    @Description("Get the part from environment")
    @Severity(SeverityLevel.NORMAL)
    public void selectPart() {

        MaterialPart materialPart = doUpdatePart();

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY)
                .setStatusCode(204)
                .setHeaders(userData.getAuthorizationHeaders())
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        materialPart.getLineItemIdentity(),
                        materialPart.getIdentity())
                .commitChanges()
                .connect()
                .post();
    }

}
