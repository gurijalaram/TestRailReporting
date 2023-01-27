package com.fieldsvalidation;

import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
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
import java.util.Arrays;
import java.util.List;

public class MountTypePinCountTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public MountTypePinCountTests() {
        super();
    }

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(getDriver().getCurrentUrl()));
    }

    @Test
    @TestRail(testCaseId = "15407")
    @Description("Verify that three variants radio buttons for Mount Type field exists")
    public void mountTypeRadioBtnTest() {
        currentUser = UserUtil.getUser();
        String fileName = "Test BOM 5.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart("460819 BK005")
            .highlightItem()
            .editSelectedBom();

        softAssertions.assertThat(editBomPage.getMountTypeButtonsSize()).isEqualTo(3);
        List<String> mountTypeLabelsActual = editBomPage.getMountTypeBtnLabels();
        softAssertions.assertThat(mountTypeLabelsActual)
            .containsExactlyInAnyOrderElementsOf(Arrays.asList("Surface Mount", "Through Hole", "Other:"));
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3223", "3217"})
    @Description("Verify Mount Type Accepted Values >=5 Characters ,Verify Pin Count and Mount Type are required fields")
    public void mountTypePinCountAreRequiredTest() {
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();

        currentUser = UserUtil.getUser();
        String fileName = "Test BOM 5.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart("460819 BK005")
            .highlightItem()
            .editSelectedBom()
            .selectOtherMountType("Hole");

        softAssertions.assertThat(editBomPage.isMountTypeWarnMsgDisplayed()).isTrue();
        softAssertions.assertThat(editBomPage.isSaveButtonDisabledDisplayed()).isTrue();

        editBomPage.selectOtherMountType(testMountTypeData)
            .enterPinCount(testPinCountData);

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = "3222")
    @Description("Verify Pin Count only accepts integer values")
    public void pinCountNeedsToBeIntTest() {
        String testMountTypeData = generateStringUtil.getRandomString();
        String testPinCountData = generateStringUtil.getRandomNumbers();

        currentUser = UserUtil.getUser();
        String fileName = "Test BOM 5.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        editBomPage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .selectMatchedPart("460819 BK005")
            .highlightItem()
            .editSelectedBom()
            .selectOtherMountType(testMountTypeData)
            .enterPinCount("123a");

        softAssertions.assertThat(editBomPage
            .isErrorMessageDisplayed("Pin Count must be a whole number")).isTrue();
        softAssertions.assertThat(editBomPage.isSaveButtonDisabledDisplayed()).isTrue();

        editBomPage.enterPinCount("12345");

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isTrue();
        softAssertions.assertAll();
    }
}
