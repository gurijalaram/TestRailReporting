package com.apriori;

import com.apriori.utils.Constants;
import com.apriori.utils.FileUploadResources;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

public class WorkorderAPITests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void loadCadMetadataAndGeneratePartImages() {
        //Object fileObject = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        new FileUploadResources().uploadLoadCadMetadataGeneratePartImages(
                "bracket_basic.prt",
                "Initial",
                "Sheet Metal"
        );
    }
}
