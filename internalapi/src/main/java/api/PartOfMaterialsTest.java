package main.java.api;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.http.builder.common.response.common.MaterialPart;
import main.java.http.builder.common.response.common.MaterialPartWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.PartsAPIEnum;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PartOfMaterialsTest extends BaseTestData {

    @Test
    @Description("Get list of parts")
    @Severity(SeverityLevel.NORMAL)
    public void getParts() {
        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.GET_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
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
                .setHeaders(authorizationHeaders)
                .setEndpoint(PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setReturnType(MaterialPartWrapper.class)
                .setStatusCode(201)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
                .setBody(materialPart)
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
    public void costPart() {

        List<String> costingIdentity = lineItem.getMaterialParts().stream()
                .map(MaterialPart::getIdentity)
                .collect(Collectors.toList());

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
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
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
                .setBody(materialPart.setAverageCost(5f))
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
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        materialPart.getLineItemIdentity(),
                        materialPart.getIdentity())
                .commitChanges()
                .connect()
                .post();
    }
}
