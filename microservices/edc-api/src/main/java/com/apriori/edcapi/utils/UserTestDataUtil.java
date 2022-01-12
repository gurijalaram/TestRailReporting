package com.apriori.edcapi.utils;

import com.apriori.apibase.services.response.objects.BillOfMaterial;
import com.apriori.apibase.services.response.objects.BillOfMaterialsWrapper;
import com.apriori.apibase.services.response.objects.BillOfSingleMaterialWrapper;
import com.apriori.apibase.services.response.objects.MaterialLineItem;
import com.apriori.apibase.services.response.objects.MaterialsLineItemsWrapper;
import com.apriori.apibase.utils.TestUtil;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.utils.http.enums.common.api.PartsAPIEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.token.TokenUtil;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserTestDataUtil extends TestUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserTestDataUtil.class);
    private String token;

    public UserDataEDC initEmptyUser() {
        UserCredentials userNamePass = UserUtil.getUser();

        return new UserDataEDC(userNamePass.getEmail(), userNamePass.getPassword());
    }

    public String getToken() {
        return token == null ? token = this.initToken() : token;
    }

    public String initToken() {
        return initToken(UserUtil.getUser());
    }

    public String initToken(UserCredentials userCredentials) {
        return new TokenUtil(userCredentials).getTokenAsString();
    }

    public UserDataEDC initBillOfMaterials() {
        UserDataEDC userDataEDC = initEmptyUser();

        userDataEDC.addWorkingIdentity(uploadTestData(userDataEDC));

        userDataEDC.setBillOfMaterials(
            getWorkingBillOfMaterialsByIdentity(
                userDataEDC.getWorkingIdentities()
            )
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
        RequestEntity requestEntity = RequestEntityUtil.init(
            PartsAPIEnum.GET_LINE_ITEMS, MaterialsLineItemsWrapper.class)
            .inlineVariables(userDataEDC.getBillOfMaterial().getIdentity());

        return (MaterialsLineItemsWrapper) HTTPRequest.build(requestEntity).get()
            .getResponseEntity();
    }

    private BillOfMaterial getBillOfMaterial(List<BillOfMaterial> billOfMaterials) {
        return billOfMaterials.get(
            new Random().nextInt(
                billOfMaterials.size()
            )
        );
    }

    private BillOfMaterialsWrapper getBillOfMaterials(UserDataEDC userDataEDC) {
        RequestEntity requestEntity = RequestEntityUtil.init(
            BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, BillOfMaterialsWrapper.class);

        return (BillOfMaterialsWrapper) HTTPRequest.build(requestEntity).get()
            .getResponseEntity();

    }

    private List<BillOfMaterial> getWorkingBillOfMaterialsByIdentity(List<String> identities) {
        List<BillOfMaterial> workingBillOfMaterials = new ArrayList<>();

        identities.forEach(identity -> {
            RequestEntity requestEntity = RequestEntityUtil.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, BillOfSingleMaterialWrapper.class)
                .inlineVariables(identity);

            workingBillOfMaterials.add(
                ((BillOfSingleMaterialWrapper) HTTPRequest.build(requestEntity).get().getResponseEntity()).getBillOfMaterial()
            );

        });

        return workingBillOfMaterials;
    }

    public void clearTestData(final UserDataEDC userDataEDC) {
        if (userDataEDC != null) {
            userDataEDC.getWorkingIdentities().forEach(identity -> {
                    RequestEntity requestEntity = RequestEntityUtil.init(
                        BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userDataEDC.getUserCredentials(), null)
                        .inlineVariables(identity);

                    validateResponseCodeByExpectingAndRealCode(HttpStatus.SC_NO_CONTENT,
                        HTTPRequest.build(requestEntity).delete().getStatusCode());
                }
            );
        }
    }

    public String uploadTestData(final UserDataEDC userDataEDC) {
        final File testData = FileResourceUtil.getResourceAsFile("testdata", "testdata/Test BOM 5.csv");

        RequestEntity requestEntity = RequestEntityUtil.init(
            BillOfMaterialsAPIEnum.POST_BILL_OF_MATERIALS, BillOfSingleMaterialWrapper.class)
            .multiPartFiles(new MultiPartFiles().use("multiPartFile", testData))
            .formParams(new FormParams().use("type", "WH"));

        ResponseWrapper<BillOfSingleMaterialWrapper> response = HTTPRequest.build(requestEntity).postMultipart();

        return response.getResponseEntity().getBillOfMaterial().getIdentity();
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
