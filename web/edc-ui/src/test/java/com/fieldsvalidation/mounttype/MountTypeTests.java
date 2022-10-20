package com.fieldsvalidation.mounttype;

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
import java.util.Arrays;
import java.util.List;

public class MountTypeTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private MatchedPartPage matchedPartPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();
    private UserCredentials currentUser;
    private SoftAssertions softAssertions = new SoftAssertions();

    public MountTypeTests() {
        super();
    }

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(getDriver().getCurrentUrl()));
    }

    @Test
    @TestRail(testCaseId = "564068")
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
    @TestRail(testCaseId = "64904")
    @Description("Verify Mount Type Accepted Values >=5 Characters ")
    public void mountTypeValueSizeTest() {
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
        softAssertions.assertAll();
    }
}
