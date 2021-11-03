package com.upload.pcba;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.pageobjects.common.EditBomPage;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.EdcUiResources;
import io.qameta.allure.Description;

import org.junit.After;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private EditBomPage editBomPage;
    private MatchedPartPage matchedPartPage;
    private GenerateStringUtil generateStringUtil = new GenerateStringUtil();


    public UploadTests() {
        super();
    }

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()));
    }

    @Test
    @TestRail(testCaseId = "1553")
    @Description("Basic workflow to upload a csv file, edit missing sections and save")
    public void testUploadBOM() {

        String fileName = "Test BOM 5.csv";
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

        matchedPartPage = editBomPage.enterMountType(testMountTypeData)
            .enterPinCount(testPinCountData)
            .clickSave();

        assertThat(matchedPartPage.getPinCountHeaderText(), is(equalTo("Pin Count")));
    }
}
