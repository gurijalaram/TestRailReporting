package com.apriori.edc.tests;

import com.apriori.apibase.services.response.objects.MaterialPart;
import com.apriori.apibase.services.response.objects.MaterialPartWrapper;
import com.apriori.edcapi.tests.util.EdcTestUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.api.PartsAPIEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
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
        RequestEntity requestEntity = RequestEntityUtil.init(PartsAPIEnum.GET_PARTS_BY_BILL_AND_LINE_IDENTITY, null)
            .inlineVariables(
                userData.getBillOfMaterial().getIdentity(),
                userData.getLineItem().getIdentity()
            );

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).get().getStatusCode());
    }

    @Test
    @Description("Post the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void postPart() {
        RequestEntity requestEntity = RequestEntityUtil.init(PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY, null)
            .inlineVariables(
                userData.getBillOfMaterial().getIdentity(),
                userData.getLineItem().getIdentity())
            .body(userData.getMaterialPart());

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).post().getStatusCode());
    }

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void costPart() {
        final List<String> costingIdentity = userData.getLineItem().getMaterialParts().stream()
            .map(MaterialPart::getIdentity)
            .collect(Collectors.toList());

        RequestEntity requestEntity = RequestEntityUtil.init(
            PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY, null)
            .inlineVariables(
                userData.getBillOfMaterial().getIdentity(),
                userData.getLineItem().getIdentity())
            .body(costingIdentity);

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_OK,
            HTTPRequest.build(requestEntity).post().getStatusCode());
    }

    @Test
    @Description("Patch the part to environment")
    @Severity(SeverityLevel.NORMAL)
    public void updatePart() {
        doUpdatePart();
    }

    private MaterialPart doUpdatePart() {
        RequestEntity requestEntity = RequestEntityUtil.init(
            PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY, MaterialPartWrapper.class)
            .inlineVariables(
                userData.getBillOfMaterial().getIdentity(),
                userData.getLineItem().getIdentity())
            .body(userData.getMaterialPart().setAverageCost(5f));

        final ResponseWrapper<MaterialPartWrapper> materialPartWrapper = HTTPRequest.build(requestEntity).post();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_CREATED, materialPartWrapper.getStatusCode());

        return materialPartWrapper.getResponseEntity().getMaterialPart();
    }

    @Test
    @Description("Get the part from environment")
    @Severity(SeverityLevel.NORMAL)
    public void selectPart() {
        MaterialPart materialPart = doUpdatePart();

        RequestEntity requestEntity = RequestEntityUtil.init(
            PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY, null)
            .inlineVariables(
                userData.getBillOfMaterial().getIdentity(),
                materialPart.getLineItemIdentity(),
                materialPart.getIdentity())
            .body(userData.getMaterialPart().setAverageCost(5f));


        ResponseWrapper<MaterialPartWrapper> materialPartWrapper = HTTPRequest.build(requestEntity).post();

        validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT, materialPartWrapper.getStatusCode());
    }
}
