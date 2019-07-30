package main.java.api;

import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.MaterialLineItem;
import main.java.http.builder.common.response.common.MaterialPart;
import main.java.http.builder.common.response.common.MaterialsLineItemsWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.http.enums.common.api.PartsAPIEnum;
import main.java.utils.MultiPartFiles;
import main.java.utils.WebDriverUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BaseTestData {

    protected static List<BillOfMaterial> billOfMaterials;
    protected static BillOfMaterial billOfMaterial;
    protected static Map<String, String> authorizationHeaders;
    protected static String token;
    protected static MaterialLineItem lineItem;
    protected static MaterialPart materialPart;

    @BeforeClass
    public static void initBillOfMaterials() {
        //TODO z: add real credentials for qa environment http://edc-api.qa.awsdev.apriori.com/
        token = new WebDriverUtils().getToken("cfrith@apriori.com", "TestEvent2018");

        authorizationHeaders = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", "EDC");
            }};

        uploadTestData();

        BillOfMaterialsWrapper billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .setHeaders(authorizationHeaders)
                .commitChanges()
                .connect()
                .get();

        billOfMaterials = billOfMaterialsWrapper.getBillOfMaterialsList();

        Assert.assertNotEquals("User doesn't has uploaded parts", billOfMaterials.size(), 0);

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

        materialPart = lineItem.getMaterialParts().get(0)
                .setUserPart(true)
                .setAverageCost(1f)
                .setManufacturerPartNumber(lineItem.getManufacturerPartNumber());
    }

    @AfterClass
    public static void clearTestData() {
        billOfMaterials.forEach(billOfMaterial -> new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterial.getIdentity())
                .setStatusCode(204)
                .commitChanges()
                .connect()
                .delete());
    }

    protected static void uploadTestData() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setMultiPartFiles(
                        new MultiPartFiles().use("multiPartFile",
                                new File(
                                        ClassLoader.getSystemClassLoader().getResource("test-data/apriori-3-items.csv").getFile())
                        ))
                .setStatusCode(201)
                .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_METERIALS)
                .commitChanges()
                .connect()
                .postMultiPart();
    }

    private static MaterialLineItem getRandomLineItemWithParts(MaterialsLineItemsWrapper materialsLineItemsWrapper) {
        MaterialLineItem materialLineItem = materialsLineItemsWrapper.getMaterialLineItems()
                .get(new Random().nextInt(materialsLineItemsWrapper.getMaterialLineItems().size()));

        if (materialLineItem.getMaterialParts().size() == 0) {
            return getRandomLineItemWithParts(materialsLineItemsWrapper);
        }

        return materialLineItem;
    }

}
