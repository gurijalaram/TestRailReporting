package com.upload.pcba;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

import java.io.File;

public class FilterPartsTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;

    public FilterPartsTests() {
        super();
    }

    @Test
    public void testFiltering() {

        String fileName = "Test BOM 5 (1).csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .filter();
    }
}
