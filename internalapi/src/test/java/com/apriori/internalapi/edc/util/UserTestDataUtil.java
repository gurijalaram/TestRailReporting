package com.apriori.internalapi.edc.util;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.response.common.BillOfMaterial;
import com.apriori.apibase.http.builder.common.response.common.BillOfMaterialsWrapper;
import com.apriori.apibase.http.builder.common.response.common.MaterialLineItem;
import com.apriori.apibase.http.builder.common.response.common.MaterialsLineItemsWrapper;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.service.RequestAreaUiAuth;
import com.apriori.apibase.http.enums.common.api.BillOfMaterialsAPIEnum;
import com.apriori.apibase.http.enums.common.api.PartsAPIEnum;
import com.apriori.apibase.utils.MultiPartFiles;
import com.apriori.utils.Util;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Random;

public class UserTestDataUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserTestDataUtil.class);

    public UserDataEDC initEmptyUser() {
        UserCredentials userNamePass = UserUtil.getUser();

      return new UserDataEDC(userNamePass.getUsername(), userNamePass.getPassword());
    }

    public UserDataEDC initBillOfMaterials() {
        UserDataEDC userDataEDC = initEmptyUser();

        uploadTestData(userDataEDC);

        userDataEDC.setBillOfMaterials(
            getBillOfMaterials(
                userDataEDC
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

        RequestEntity requestEntity = RequestEntity.init(
                PartsAPIEnum.GET_LINE_ITEMS, userDataEDC.getUserCredentials(), MaterialsLineItemsWrapper.class)
                .setInlineVariables(userDataEDC.getBillOfMaterial().getIdentity());

        return (MaterialsLineItemsWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth())
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
                BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS, userDataEDC.getUserCredentials(), BillOfMaterialsWrapper.class);

        return (BillOfMaterialsWrapper) GenericRequestUtil.get(requestEntity, new RequestAreaUiAuth())
                .getResponseEntity();
    }

    public void clearTestData(final UserDataEDC userDataEDC) {
        userDataEDC.getBillOfMaterials().forEach(billOfMaterial ->
                GenericRequestUtil.delete(
                        RequestEntity.init(BillOfMaterialsAPIEnum.GET_BILL_OF_MATERIALS_IDENTITY, userDataEDC.getUserCredentials(), null)
                        .setInlineVariables(billOfMaterial.getIdentity())
                        .setStatusCode(HttpStatus.SC_NO_CONTENT),
                        new RequestAreaUiAuth()
                )
        );
    }

    public void uploadTestData(final UserDataEDC userDataEDC) {
        final File testData = Util.getLocalResourceFile("test-data/apriori-4-items.csv");

        RequestEntity requestEntity = RequestEntity.init(
                BillOfMaterialsAPIEnum.POST_BILL_OF_MATERIALS, userDataEDC.getUserCredentials(), null)
                .setMultiPartFiles(new MultiPartFiles().use("multiPartFile", testData));

        GenericRequestUtil.postMultipart(requestEntity, new RequestAreaUiAuth());
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
