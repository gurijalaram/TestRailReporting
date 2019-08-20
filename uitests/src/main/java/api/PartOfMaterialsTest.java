package main.java.api;

import io.qameta.allure.Description;
import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.MaterialLineItem;
import main.java.http.builder.common.response.common.MaterialPart;
import main.java.http.builder.common.response.common.MaterialPartWrapper;
import main.java.http.builder.common.response.common.MaterialsLineItemsWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.http.enums.common.api.PartsAPIEnum;
import main.java.utils.WebDriverUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PartOfMaterialsTest {

    private static MaterialLineItem lineItem;
    private static BillOfMaterial billOfMaterial;
    private static Map<String, String> authorizationHeaders;
    private static String token;
    private static MaterialPart materialPart;

    @BeforeClass
    public static void getLineItems() {
        //TODO z: add real credentials for qa environment http://edc-api.qa.awsdev.apriori.com/
        token = new WebDriverUtils().getToken("email", "password");

        authorizationHeaders = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", "EDC");
            }};

        BillOfMaterialsWrapper billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setHeaders(authorizationHeaders)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();

        billOfMaterial = billOfMaterialsWrapper.getBillOfMaterialsList().get(new Random().nextInt(billOfMaterialsWrapper.getBillOfMaterialsList().size()));

        MaterialsLineItemsWrapper materialsLineItemsWrapper = (MaterialsLineItemsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setInlineVariables(billOfMaterial.getIdentity())
                .setHeaders(authorizationHeaders)
                .setEndpoint(PartsAPIEnum.GET_LINE_ITEMS)
                .setReturnType(MaterialsLineItemsWrapper.class)
                .commitChanges()
                .connect()
                .get();

        lineItem = getRandomLineItemWithParts(materialsLineItemsWrapper);


        materialPart = new MaterialPart()
                .setUserPart(true)
                .setAverageCost(1f)
                .setManufacturerPartNumber(lineItem.getManufacturerPartNumber());
    }

    private static MaterialLineItem getRandomLineItemWithParts(MaterialsLineItemsWrapper materialsLineItemsWrapper) {
        MaterialLineItem materialLineItem = materialsLineItemsWrapper.getMaterialLineItems()
                .get(new Random().nextInt(materialsLineItemsWrapper.getMaterialLineItems().size()));

        if (materialLineItem.getMaterialParts().size() == 0) {
            return getRandomLineItemWithParts(materialsLineItemsWrapper);
        }

        return materialLineItem;
    }


    @Test
    @Description("Get list of parts")
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
    public void costPart() {

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
                .setBody(Arrays.asList(lineItem.getIdentity()))
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Patch the part to environment")
    public void updatePart() {

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.PATCH_UPDATE_PART_BY_BILL_LINE_AND_PART_IDENTITY)
                .setReturnType(MaterialPartWrapper.class)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity(),
                        lineItem.getMaterialParts().get(new Random().nextInt(lineItem.getMaterialParts().size())).getIdentity())
                .setBody(materialPart)
                .commitChanges()
                .connect()
                .patch();
    }

    @Test
    @Description("Get the part from environment")
    public void selectPart() {

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY)
                .setStatusCode(204)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity(),
                        lineItem.getMaterialParts().get(new Random().nextInt(lineItem.getMaterialParts().size())).getIdentity())
                .commitChanges()
                .connect()
                .post();
    }
}
