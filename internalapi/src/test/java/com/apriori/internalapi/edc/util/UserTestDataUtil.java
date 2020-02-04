package com.apriori.internalapi.edc.util;

import com.apriori.apibase.http.builder.common.response.common.BillOfMaterial;
import com.apriori.apibase.http.builder.common.response.common.BillOfMaterialsWrapper;
import com.apriori.apibase.http.builder.common.response.common.MaterialLineItem;
import com.apriori.apibase.http.builder.common.response.common.MaterialsLineItemsWrapper;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.apibase.http.enums.common.api.PartsAPIEnum;
import com.apriori.apibase.utils.MultiPartFiles;
import com.apriori.apibase.utils.WebDriverUtils;
import com.apriori.utils.Util;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserTestDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserTestDataUtil.class);

    public UserDataEDC initEmptyUser() {
        UserCredentials userNamePass = UserUtil.getUser();

        UserDataEDC userDataEDC = new UserDataEDC(userNamePass.getUsername(), userNamePass.getPassword());

        userDataEDC.setTokenAndInitAuthorizationHeaders(
            new WebDriverUtils()
                .getToken(userNamePass.getUsername(),
                    userNamePass.getPassword()
                )
        );

        return userDataEDC;
    }

    public UserDataEDC initBillOfMaterials() {
        UserDataEDC userDataEDC = initEmptyUser();

        uploadTestData(userDataEDC);

        userDataEDC.setBillOfMaterials(
            getBillOfMaterials(
                userDataEDC.getAuthorizationHeaders()
            ).getBillOfMaterialsList()
        );

        userDataEDC.setBillOfMaterial(
            getBillOfMaterial(userDataEDC.getBillOfMaterials())
        );

        userDataEDC.setLineItem(
            getRandomLineItemWithParts(getMaterialsLineItemWrapper(userDataEDC))
        );

        userDataEDC.setMaterialPart(
            userDataEDC.getLineItem().getMaterialParts().get(0)
                .setUserPart(true)
                .setAverageCost(1f)
                .setManufacturerPartNumber(userDataEDC.getLineItem().getManufacturerPartNumber())
        );

        return userDataEDC;
    }

    private MaterialsLineItemsWrapper getMaterialsLineItemWrapper(UserDataEDC userDataEDC) {
        return (MaterialsLineItemsWrapper) new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setInlineVariables(userDataEDC.getBillOfMaterial().getIdentity())
            .setHeaders(userDataEDC.getAuthorizationHeaders())
            .setEndpoint(PartsAPIEnum.GET_LINE_ITEMS)
            .setReturnType(MaterialsLineItemsWrapper.class)
            .commitChanges()
            .connect()
            .get();
    }

    private BillOfMaterial getBillOfMaterial(List<BillOfMaterial> billOfMaterials) {
        return billOfMaterials.get(
            new Random().nextInt(
                billOfMaterials.size()
            )
        );
    }

    private BillOfMaterialsWrapper getBillOfMaterials(Map<String, String> authorizationHeaders) {
        return (BillOfMaterialsWrapper) new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS)
            .setReturnType(BillOfMaterialsWrapper.class)
            .setHeaders(authorizationHeaders)
            .commitChanges()
            .connect()
            .get();
    }

    public void clearTestData(final UserDataEDC userDataEDC) {
        userDataEDC.getBillOfMaterials().forEach(billOfMaterial -> new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userDataEDC.getAuthorizationHeaders())
            .setEndpoint(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY)
            .setInlineVariables(billOfMaterial.getIdentity())
            .setStatusCode(204)
            .commitChanges()
            .connect()
            .delete());
    }

    public void uploadTestData(final UserDataEDC userDataEDC) {

        final File testData = Util.getLocalResourceFile("test-data/apriori-4-items.csv");

        new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(userDataEDC.getAuthorizationHeaders())
            .setMultiPartFiles(
                new MultiPartFiles().use("multiPartFile", testData)
            )
            .setStatusCode(201)
            .setEndpoint(BillOfMaterialsAPIEnum.POST_BILL_OF_MATERIALS)
            .commitChanges()
            .connect()
            .postMultiPart();
    }

    private MaterialLineItem getRandomLineItemWithParts(MaterialsLineItemsWrapper materialsLineItemsWrapper) {
        MaterialLineItem materialLineItem = materialsLineItemsWrapper.getMaterialLineItems()
            .get(new Random().nextInt(materialsLineItemsWrapper.getMaterialLineItems().size()));

        if (materialLineItem.getMaterialParts().size() == 0) {
            return getRandomLineItemWithParts(materialsLineItemsWrapper);
        }

        return materialLineItem;
    }
}
