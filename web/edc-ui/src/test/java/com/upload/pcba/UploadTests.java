package com.upload.pcba;

import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.EdcUiResources;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private MatchedPartPage matchedPartPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public UploadTests() {
        super();
    }

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(getDriver().getCurrentUrl()));
    }

    @Test
    @TestRail(testCaseId = "1553")
    @Description("Basic workflow to upload a csv file, edit missing sections and save")
    public void testUploadBOM() {
        currentUser = UserUtil.getUser();

        String fileName = "Test BOM 5.csv";
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart()
            .highlightItem()
            .editSelectedBom();

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isEqualTo(false);

        matchedPartPage = editBomPage
            .selectMountType("Other:", testMountTypeData)
            .enterPinCount(testPinCountData)
            .clickSave();

        softAssertions.assertThat(matchedPartPage.getPinCountHeaderText()).isEqualTo("Pin Count");

        softAssertions.assertAll();
    }
}
