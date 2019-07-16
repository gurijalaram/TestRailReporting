package main.java.api;

import main.java.http.builder.common.response.common.*;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import main.java.http.enums.common.api.PartsAPIEnum;
import main.java.utils.WebDriverUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PartOfMaterialsTest {

    private static MaterialLineItem lineItem;
    private static BillOfMaterial billOfMaterial;
    private static Map<String, String> authorizationHeaders;
    private static String token;

    @BeforeClass
    public static void getLineItems(){
        //TODO z: add real credentials for qa environment http://edc-api.qa.awsdev.apriori.com/
        token = WebDriverUtils.init().getToken("email", "passsword");
        authorizationHeaders =  new HashMap<String, String>() {{
            put("Authorization", "Bearer " + token);
            put("ap-cloud-context", "EDC");
            put("Content-Type", "*/*");
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


    }

    private static MaterialLineItem getRandomLineItemWithParts(MaterialsLineItemsWrapper materialsLineItemsWrapper) {
        MaterialLineItem materialLineItem = materialsLineItemsWrapper.getMaterialLineItems()
                .get(new Random().nextInt(materialsLineItemsWrapper.getMaterialLineItems().size()));

        if(materialLineItem.getMaterialParts().size() == 0){
            return getRandomLineItemWithParts(materialsLineItemsWrapper);
        }

        return materialLineItem;
    }



    @Test
    public void getParts(){

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
    public void postPart(){

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setReturnType(MaterialPart.class)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
//                TODO z: add test data
//                .setPayloadJSON()
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    public void costPart(){

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_COST_PARTS_BY_BILL_AND_LINE_IDENTITY)
                .setReturnType(MaterialPart.class)
                .setHeaders(authorizationHeaders)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity())
//                TODO z: add test data
//                .setPayloadJSON()
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    public void updatePart(){

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.PATCH_UPDATE_PART_BY_BILL_LINE_AND_PART_IDENTITY)
                .setReturnType(MaterialPart.class)
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity(),
                        lineItem.getMaterialParts().get(new Random().nextInt(lineItem.getMaterialParts().size())).getIdentity())
//                TODO z: add test data
//                .setPayloadJSON()
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    public void selectPart(){

        new HTTPRequest().unauthorized()
                .customizeRequest()
                .setEndpoint(PartsAPIEnum.POST_SELECT_PART_BY_BILL_LINE_AND_PART_IDENTITY)
                .setStatusCode(204)
                .setHeaders(new HashMap<String, String>() {{
                        put("Authorization", "Bearer " + token);
                        put("ap-cloud-context", "EDC");
                    }})
                .setInlineVariables(
                        billOfMaterial.getIdentity(),
                        lineItem.getIdentity(),
                        lineItem.getMaterialParts().get(new Random().nextInt(lineItem.getMaterialParts().size())).getIdentity())
                .commitChanges()
                .connect()
                .post();
    }
}
