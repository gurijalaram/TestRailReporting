package com.upload.pcba;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.BillOfMaterialsPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private BillOfMaterialsPage billOfMaterialsPage;

    public UploadTests() {
        super();
    }

    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();

    @Test
    @TestRail(testCaseId = "1553")
    @Description("Basic workflow to upload a csv file, edit missing sections and save")
    public void testUploadBOM() {

        String fileName = "Test BOM 5_v1.0.csv";
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();

        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(UserUtil.getUser())
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart()
            .highlightItem()
            .editSelectedBom();

        assertThat(editBomPage.isSaveButtonEnabled(), not(true));

        billOfMaterialsPage = editBomPage.enterMountType(testMountTypeData)
            .enterPinCount(testPinCountData)
            .clickSave();
    }
}
