package com.upload.pcba;

import com.apriori.FileResourceUtil;
import com.apriori.GenerateStringUtil;
import com.apriori.TestBaseUI;
import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.ElectronicsDataCollectionPage;
import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.pageobjects.pages.login.UploadedFilePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import com.utils.EdcUiResources;
import com.utils.RightClickOptionEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

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

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()));
    }

    @Test
    @TestRail(id = 1553)
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

    @Ignore("ignoring temporarily as qa-test gets 500 error while uploading wireHarness BOM")
    @Test
    @TestRail(id = 13253)
    @Description("User is able to upload a Wire Harness BOM directly to EDC on the cloud")
    public void uploadWireHarnessBOMTest() {
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();

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

    @Ignore("Ignored due to file location")
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

        String bomIdName =
            electronicsDataCollectionPage.rightClickOnFirstBomAndChooseOption(RightClickOptionEnum.EXPORT);
        String filePath = downloadPath + bomIdName + ".csv";

        softAssertions.assertThat(FileResourceUtil.deleteFileWhenAppears(Paths.get(filePath), 3)).isTrue();
        softAssertions.assertAll();
    }
}
