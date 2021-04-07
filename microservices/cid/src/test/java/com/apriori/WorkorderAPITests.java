package com.apriori;

import com.apriori.utils.Constants;
import com.apriori.utils.FileUploadResources;

import com.apriori.utils.TestRail;

import io.qameta.allure.Description;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.categories.CidAPITest;

public class WorkorderAPITests {

    @BeforeClass
    public static void getAuthorizationToken() {
        Constants.getDefaultUrl();
    }

    @Test
    @Category(CidAPITest.class)
    @TestRail(testCaseId = "6933")
    @Description("Upload a part, load CAD Metadata, and generate part images")
    public void loadCadMetadataAndGeneratePartImages() {
        new FileUploadResources().uploadLoadCadMetadataGeneratePartImages(
                "bracket_basic.prt",
                "Initial",
                "Sheet Metal"
        );
    }
}
