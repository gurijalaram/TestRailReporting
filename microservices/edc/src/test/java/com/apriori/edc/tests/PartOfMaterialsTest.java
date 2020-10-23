package com.apriori.edc.tests;

import com.apriori.apibase.services.response.objects.MaterialPart;
import com.apriori.apibase.services.response.objects.MaterialPartWrapper;
import com.apriori.edc.tests.util.EdcTestUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.enums.common.api.PartsAPIEnum;
import com.apriori.utils.http.utils.ResponseWrapper;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PartOfMaterialsTest extends EdcTestUtil {

    @Test
    @Description("Get list of parts")
    @Severity(SeverityLevel.NORMAL)
    public void getParts() {
        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.GET_PARTS_BY_BILL_AND_LINE_IDENTITY, userData.getUserCredentials(), null)
                .setInlineVariables(
                        userData.getBillOfMaterial().getIdentity(),
                        userData.getLineItem().getIdentity()
                )
                .setAutoLogin(true)
                .setToken(token);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getStatusCode());
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
                .setBody(userData.getMaterialPart())
                .setAutoLogin(true)
                .setToken(token);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED,
                GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getStatusCode());
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
                .setBody(costingIdentity)
                .setAutoLogin(true)
                .setToken(token);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
                GenericRequestUtil.post(requestEntity, new RequestAreaApi()).getStatusCode());
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
                .setBody(userData.getMaterialPart().setAverageCost(5f))
                .setAutoLogin(true)
                .setToken(token);

        final ResponseWrapper<MaterialPartWrapper> materialPartWrapper = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

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
                .setBody(userData.getMaterialPart().setAverageCost(5f))
                .setAutoLogin(true)
                .setToken(token);

        ResponseWrapper<MaterialPartWrapper> materialPartWrapper = GenericRequestUtil.post(requestEntity, new RequestAreaApi());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, materialPartWrapper.getStatusCode());
    }
}
