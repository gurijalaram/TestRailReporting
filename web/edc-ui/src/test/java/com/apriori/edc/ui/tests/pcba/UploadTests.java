package com.apriori.edc.ui.tests.pcba;

import com.apriori.edc.api.utils.BillOfMaterialsUtil;
import com.apriori.edc.ui.common.EditBomPage;
import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.login.ElectronicsDataCollectionPage;
import com.apriori.edc.ui.pageobjects.login.MatchedPartPage;
import com.apriori.edc.ui.pageobjects.login.UploadedFilePage;
import com.apriori.edc.ui.utils.EdcUiResources;
import com.apriori.edc.ui.utils.RightClickOptionEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UploadTests extends TestBaseUI {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private MatchedPartPage matchedPartPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;
    private UploadedFilePage uploadedFilePage;

    private ElectronicsDataCollectionPage electronicsDataCollectionPage;
    private SoftAssertions softAssertions = new SoftAssertions();

    public UploadTests() {
        super();
    }

    @AfterEach
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()), currentUser);
    }

    @Test
    @TestRail(id = 1553)
    @Description("Basic workflow to upload a csv file, edit missing sections and save")
    public void testUploadBOM() {
        currentUser = UserUtil.getUser();

        String fileName = "Test BOM 5.csv";
        String testMountTypeData = generateStringUtil.getRandomStringSpecLength(12);
        String testPinCountData = generateStringUtil.getRandomNumbersSpecLength(8);
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart("460819 BK005")
            .highlightItem()
            .editSelectedBom();

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isEqualTo(false);

        matchedPartPage = editBomPage.enterMountTypeOldVer(testMountTypeData)
            .enterPinCount(testPinCountData)
            .clickSave();

        softAssertions.assertThat(matchedPartPage.getPinCountHeaderText()).isEqualTo("Pin Count");
        softAssertions.assertThat(matchedPartPage.getMountTypeHeaderText()).isEqualTo("Mount Type");

        softAssertions.assertAll();
    }

    @Disabled("ignoring temporarily as qa-test gets 500 error while uploading wireHarness BOM")
    @Test
    @TestRail(id = 13253)
    @Description("User is able to upload a Wire Harness BOM directly to EDC on the cloud")
    public void uploadWireHarnessBOMTest() {
        String testMountTypeData = generateStringUtil.getRandomStringSpecLength(12);
        String testPinCountData = generateStringUtil.getRandomNumbersSpecLength(8);

        currentUser = UserUtil.getUser();
        String fileName = "Wire Harness BOM.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        uploadedFilePage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA();

        softAssertions.assertThat(uploadedFilePage.getBomTitleName()).isEqualTo("WIRE-HARNESS-PETE");
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 1731)
    @Description("BOM can be deleted from main page")
    public void deleteUploadedBOMTest() {
        int numberOfBomAfterUpload;
        int numberOfBomAfterDelete;

        currentUser = UserUtil.getUser();
        String fileName = "Test BOM 7.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        electronicsDataCollectionPage = loginPage.login(currentUser);

        numberOfBomAfterUpload = electronicsDataCollectionPage.getNumberOfLoadedBOMs();
        electronicsDataCollectionPage.rightClickOnFirstBomAndChooseOption(RightClickOptionEnum.DELETE);
        numberOfBomAfterDelete = numberOfBomAfterUpload - 1;
        softAssertions.assertThat(electronicsDataCollectionPage.getNumberOfLoadedBOMs(numberOfBomAfterDelete))
            .isEqualTo(numberOfBomAfterDelete);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 1732)
    @Description("BOM can be exported to file from main page")
    public void exportUploadedBOMTest() {
        currentUser = UserUtil.getUser();
        String fileName = "Test BOM 7.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        uploadedFilePage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA();

        electronicsDataCollectionPage =
            uploadedFilePage.backToElectronicsDataCollectionPage();

        File bomIdName = electronicsDataCollectionPage.rightClickOnFirstBomAndChooseOption(RightClickOptionEnum.EXPORT);

        softAssertions.assertThat(bomIdName.length()).isGreaterThan(0);
        softAssertions.assertAll();
    }
}
