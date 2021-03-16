package com.apriori;

import com.apriori.apibase.utils.APIAuthentication;
import com.apriori.utils.Constants;
import com.apriori.utils.FileUploadResources;
import com.apriori.utils.users.UserUtil;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

public class WorkorderAPITests {

    private final HashMap<String, String> token = new APIAuthentication()
            .initAuthorizationHeaderNoContent(UserUtil.getUser().getUsername());

    @BeforeClass
    public static void testSetup() {
        Constants.getDefaultUrl();
    }

    @Test
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void loadCadMetadataAndGeneratePartImages() {
        //Object fileObject = JsonManager.deserializeJsonFromFile(FileResourceUtil.getResourceAsFile("CreatePartData.json").getPath(), NewPartRequest.class);

        new FileUploadResources().uploadLoadCadMetadataGeneratePartImages(
                token,
                "bracket_basic.prt",
                "Initial",
                "Sheet Metal"
        );
    }
}
