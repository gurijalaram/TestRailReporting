package com.upload.pcba;

import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {

    UserCredentials currentUser;
    private File resourceFile;
    private EdcAppLoginPage loginPage;

    @Test
    public void testUploadBOM () {
        resourceFile = FileResourceUtil.getResourceAsFile("Test BOM 5 (1).csv");
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponent(resourceFile)
            .clickUploadPCBA();
    }
}
