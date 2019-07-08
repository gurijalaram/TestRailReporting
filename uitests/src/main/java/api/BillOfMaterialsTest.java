package main.java.api;

import main.java.http.builder.common.response.common.BillOfMaterial;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.BillOfSingleMaterialWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class BillOfMaterialsTest {

    private static BillOfMaterialsWrapper billOfMaterialsWrapper;
    private static List<BillOfMaterial> billOfMaterials;

    @BeforeClass
    public static void initBillOfMaterials() {
        billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();

        billOfMaterials = billOfMaterialsWrapper.getBillOfMaterialsList();
    }

    @Test
    public void getBillOfMaterials() {
        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();
    }

    @Test
    public void getBillOfMaterialsByIdentity() {

        BillOfSingleMaterialWrapper billOfSingleMaterialWrapper = (BillOfSingleMaterialWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterials.get(new Random().nextInt(billOfMaterials.size())).getIdentity())
                .setReturnType(BillOfSingleMaterialWrapper.class)
                .commitChanges()
                .connect()
                .get();

        System.out.println(billOfSingleMaterialWrapper.getBillOfMaterial().getName());
    }

    @Test
    public void deleteBillOfMaterialsByIdentity() {

        BillOfMaterialsWrapper billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();

        List<BillOfMaterial> materials = billOfMaterialsWrapper.getBillOfMaterialsList();

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(materials.get(new Random().nextInt(materials.size())).getIdentity())
                .setStatusCode(204)
                .commitChanges()
                .connect()
                .delete();
    }

    @Test
    public void exportBillOfMaterialsByIdentity() {
        BillOfMaterialsWrapper billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();

        List<BillOfMaterial> materials = billOfMaterialsWrapper.getBillOfMaterialsList();

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.EXPORT_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(materials.get(new Random().nextInt(materials.size())).getIdentity())
                .commitChanges()
                .connect()
                .post();
    }

    @Test
    public void postBillOfMaterials() {
        BillOfMaterialsWrapper billOfMaterialsWrapper = (BillOfMaterialsWrapper) new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterialsWrapper.class)
                .commitChanges()
                .connect()
                .get();


        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_METERIALS)
                .setReturnType(BillOfMaterial.class)
//                TODO z: add test file
//                .setMultiPartFiles()
                .commitChanges()
                .connect()
                .post();
    }
}
