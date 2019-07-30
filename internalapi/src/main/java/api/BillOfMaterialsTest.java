package main.java.api;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import main.java.http.builder.common.response.common.BillOfMaterialsWrapper;
import main.java.http.builder.common.response.common.BillOfSingleMaterialWrapper;
import main.java.http.builder.service.HTTPRequest;
import main.java.http.enums.common.api.BillOfMaterialsAPIEnum;
import org.junit.Test;

import java.util.Random;

public class BillOfMaterialsTest extends BaseTestData {

    @Test
    @Description("Get list bill of materials")
    @Severity(SeverityLevel.NORMAL)
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
    @Severity(SeverityLevel.NORMAL)
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
    @Severity(SeverityLevel.NORMAL)
    public void deleteBillOfMaterialsByIdentity() {
        final int deleteIndex = new Random().nextInt(billOfMaterials.size());

        new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setHeaders(authorizationHeaders)
                .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_METERIALS_IDENTITY)
                .setInlineVariables(billOfMaterials.get(deleteIndex).getIdentity())
                .setStatusCode(204)
                .commitChanges()
                .connect()
                .delete();

        billOfMaterials.remove(deleteIndex);
    }

    @Test
    @Description("Export bill of material by identity")
    @Severity(SeverityLevel.NORMAL)
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
    @Severity(SeverityLevel.NORMAL)
    public void postBillOfMaterials() {
        uploadTestData();
    }

}
