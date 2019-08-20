package main.java.api;

import io.qameta.allure.Description;
import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.BillOfSingleMaterialWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.utils.MultiPartFiles;
import main.java.utils.WebDriverUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BillOfMaterialsTest {

    private static Logger logger = LoggerFactory.getLogger(BillOfMaterialsTest.class);

    private static List<BillOfMaterial> billOfMaterials;
    private static Map<String, String> authorizationHeaders;
    private static String token;

    @BeforeClass
    public static void initBillOfMaterials() {
        //TODO z: add real credentials for qa environment http://edc-api.qa.awsdev.apriori.com/
        token = new WebDriverUtils().getToken("email@apriori.com", "pass");

        authorizationHeaders = new HashMap<String, String>() {{
                put("Authorization", "Bearer " + token);
                put("ap-cloud-context", "EDC");
            }};

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
    }

    @Test
    @Description("Get list bill of materials")
    public void getBillOfMaterials() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();
    }

    @Test
    @Description("Get list bill of materials by identity")
    public void getBillOfMaterialsByIdentity() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterials.get(new Random().nextInt(billOfMaterials.size())).getIdentity())
                .setReturnType(BillOfSingleMaterialWrapper.class)
                .commitChanges()
                .connect()
                .get();

    }

    @Test
    @Description("Delete list bill of materials by identity")
    public void deleteBillOfMaterialsByIdentity() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterials.get(new Random().nextInt(billOfMaterials.size())).getIdentity())
                .setStatusCode(204)
                .commitChanges()
                .connect()
                .delete();
    }

    @Test
    @Description("Export bill of material by identity")
    public void exportBillOfMaterialsByIdentity() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.EXPORT_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterials.get(new Random().nextInt(billOfMaterials.size())).getIdentity())
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    @Description("Post bill of material")
    public void postBillOfMaterials() {

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setMultiPartFiles(
                        new MultiPartFiles().use("multiPartFile",
                                new File(
                                        getClass().getClassLoader().getResource(
                                                "test-data/apriori-3-items.csv").getFile()
                                ))
                )
                .setStatusCode(201)
                .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_METERIALS)
                .commitChanges()
                .connect()
                .postMultiPart();

    }
}
