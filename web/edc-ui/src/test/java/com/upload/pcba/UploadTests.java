package com.upload.pcba;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.BillOfMaterialsPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {

    UserCredentials currentUser;
    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private BillOfMaterialsPage billOfMaterialsPage;

    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Test
    public void testUploadBOM () {

        String fileName = "Test BOM 5_v1.0.csv";
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();

        resourceFile = FileResourceUtil.getResourceAsFile(fileName);
        currentUser = UserUtil.getUser();

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(UserUtil.getUser())
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .clickItemOne()
            .highlightItem()
            .editSelectedBom();

        assertThat(editBomPage.isSaveButtonEnabled(), not(true));

        billOfMaterialsPage = editBomPage.enterMountType(testMountTypeData)
            .enterPinCount(testPinCountData)
            .clickSave();
    }
}
