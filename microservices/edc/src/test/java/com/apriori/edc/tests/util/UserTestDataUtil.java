package com.apriori.edc.tests.util;

import com.apriori.apibase.services.ats.apicalls.SecurityManager;
import com.apriori.apibase.services.response.objects.BillOfMaterial;
import com.apriori.apibase.services.response.objects.BillOfMaterialsWrapper;
import com.apriori.apibase.services.response.objects.BillOfSingleMaterialWrapper;
import com.apriori.apibase.services.response.objects.MaterialLineItem;
import com.apriori.apibase.services.response.objects.MaterialsLineItemsWrapper;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.utils.http.enums.common.api.PartsAPIEnum;
import com.apriori.utils.http.utils.FormParams;
import com.apriori.utils.http.utils.MultiPartFiles;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserTestDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserTestDataUtil.class);
    private String token;

    public UserDataEDC initEmptyUser() {
        UserCredentials userNamePass = UserUtil.getUser();

        return new UserDataEDC(userNamePass.getUsername(), userNamePass.getPassword());
    }

    public String getToken() {
        return token == null ? token = this.initToken() : token;
    }

    public String initToken() {
        return initToken(UserUtil.getUser());
    }

    public String initToken(UserCredentials userCredentials) {
        return SecurityManager.retriveJwtToken(
                Constants.getAtsServiceHost(),
                HttpStatus.SC_CREATED,
                userCredentials.getUsername().split("@")[0],
                userCredentials.getUsername(),
                Constants.getAtsTokenIssuer(),
                Constants.getAtsTokenSubject());
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

        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.GET_LINE_ITEMS, userDataEDC.getUserCredentials(), MaterialsLineItemsWrapper.class)
                .setInlineVariables(userDataEDC.getBillOfMaterial().getIdentity())
                .setToken(this.getToken())
                .setAutoLogin(true);

        return (MaterialsLineItemsWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaApi())
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

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userDataEDC.getUserCredentials(), BillOfMaterialsWrapper.class)
                .setToken(this.getToken())
                .setAutoLogin(true);

        return (BillOfMaterialsWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaApi())
                .getResponseEntity();

    }

    private List<BillOfMaterial> getWorkingBillOfMaterialsByIdentity(List<String> identities) {
        List<BillOfMaterial> workingBillOfMaterials = new ArrayList<>();

        identities.forEach(identity -> {

            RequestEntity requestEntity = RequestEntity.init(
                    BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, UserUtil.getUser(), BillOfSingleMaterialWrapper.class)
                    .setInlineVariables(identity)
                    .setToken(token)
                    .setAutoLogin(true);

            workingBillOfMaterials.add(
                    ((BillOfSingleMaterialWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaApi()).getResponseEntity()).getBillOfMaterial()
            );

        });

        return workingBillOfMaterials;
    }

    public void clearTestData(final UserDataEDC userDataEDC) {
        userDataEDC.getWorkingIdentities().forEach(identity ->
                GenericRequestUtil.delete(
                        RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userDataEDC.getUserCredentials(), null)
                                .setInlineVariables(identity)
                                .setStatusCode(HttpStatus.SC_NO_CONTENT)
                                .setToken(this.getToken())
                                .setAutoLogin(true),
                        new RequestAreaApi()
                )
        );
    }

    public String uploadTestData(final UserDataEDC userDataEDC) {
        final File testData = FileResourceUtil.getResourceAsFile("test_data", "apriori-4-items.csv");

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.POST_BILL_OF_MATERIALS, userDataEDC.getUserCredentials(), BillOfSingleMaterialWrapper.class)
                .setMultiPartFiles(new MultiPartFiles().use("multiPartFile", testData))
                .setToken(this.getToken())
                .setAutoLogin(true)
                .setFormParams(new FormParams().use("type", "WH"));

        return ((BillOfSingleMaterialWrapper) GenericRequestUtil.postMultipart(requestEntity, new RequestAreaApi()).getResponseEntity()).getBillOfMaterial().getIdentity();
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
