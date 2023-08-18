package com.apriori.fieldsvalidation;

import com.apriori.common.EditBomPage;
import com.apriori.edc.utils.BillOfMaterialsUtil;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pageobjects.login.EdcAppLoginPage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.EdcUiResources;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MountTypePinCountTests extends TestBaseUI {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public MountTypePinCountTests() {
        super();
    }

    @AfterEach
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()));
    }

    @Test
    @Disabled("this feature is not on qa-test yet")
    @TestRail(id = 15407)
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
    @TestRail(id = {3223, 3217})
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
            .enterMountTypeOldVer("Hole");

        softAssertions.assertThat(editBomPage.isMountTypeWarnMsgDisplayed()).isTrue();
        softAssertions.assertThat(editBomPage.isSaveButtonDisabledDisplayed()).isTrue();

        editBomPage.enterMountTypeOldVer(testMountTypeData)
            .enterPinCount(testPinCountData);

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 3222)
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
            .enterMountTypeOldVer(testMountTypeData)
            .enterPinCount("123a");

        softAssertions.assertThat(editBomPage
            .isErrorMessageDisplayed("Pin Count must be a whole number")).isTrue();
        softAssertions.assertThat(editBomPage.isSaveButtonDisabledDisplayed()).isTrue();

        editBomPage.enterPinCount("12345");

        softAssertions.assertThat(editBomPage.isSaveButtonEnabled()).isTrue();
        softAssertions.assertAll();
    }
}
