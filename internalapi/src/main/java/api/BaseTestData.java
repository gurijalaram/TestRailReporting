package main.java.api;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.MaterialLineItem;
import main.java.http.builder.common.response.common.MaterialPart;
import main.java.http.builder.common.response.common.MaterialsLineItemsWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.http.enums.common.api.PartsAPIEnum;
import main.java.pages.explore.LogOutPage;
import main.java.utils.MultiPartFiles;
import main.java.utils.WebDriverUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class BaseTestData {

    private static final Logger logger = LoggerFactory.getLogger(LogOutPage.class);

    private Map<String, String> autorizedUsers = new HashMap<>();

    protected static String username;
    protected static List<BillOfMaterial> billOfMaterials;
    protected static BillOfMaterial billOfMaterial;
    protected static Map<String, String> authorizationHeaders;
    protected static String token;
    protected static MaterialLineItem lineItem;
    protected static MaterialPart materialPart;

    public static void initBillOfMaterials(String username, String password) {

        System.out.println("STEP 2 init");

        if(token != null){
            return;
        }

        System.out.println("**************** " + username);
        //TODO z: add real credentials for qa environment http://edc-api.qa.awsdev.apriori.com/
//        token = new WebDriverUtils().getToken(testUser, "TestEvent2018");
        token = new WebDriverUtils().getToken(username, password);

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

        final File testData = getResourceFile("test-data/apriori-3-items.csv");

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setMultiPartFiles(
                        new MultiPartFiles().use("multiPartFile", testData)
                )
                .setStatusCode(201)
                .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_METERIALS)
                .commitChanges()
                .connect()
                .postMultiPart();
    }

    private static File getResourceFile(String resourceFileName) {
        try {
            return new File(
                    URLDecoder.decode(
                            ClassLoader.getSystemResource(resourceFileName).getFile(),
                            "UTF-8"
                    )
            );
        } catch (UnsupportedEncodingException e) {
            logger.error(String.format("Resource file: %s was not fount", resourceFileName));
            throw new IllegalArgumentException();
        }
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
