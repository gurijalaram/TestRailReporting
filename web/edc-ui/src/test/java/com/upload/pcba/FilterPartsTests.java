package com.upload.pcba;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.edcapi.utils.BillOfMaterialsUtil;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.UploadedFilePage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CostStatusEnum;
import com.utils.EdcUiResources;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class FilterPartsTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private UploadedFilePage uploadedFilePage;
    private UserCredentials currentUser;

    public FilterPartsTests() {
        super();
    }

    @After
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(getDriver().getCurrentUrl()));
    }

    @Test
    @TestRail(testCaseId = "1727")
    @Description("BOM - Filter Parts - Cost Status - Available Drop Down options")
    public void testFiltering() {
        currentUser = UserUtil.getUser();

        String fileName = "Test BOM 5.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        uploadedFilePage = loginPage.login(currentUser)
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .filterDropdown()
            .selectCostStatus(CostStatusEnum.MATCH_COMPLETE)
            .selectSearch("4608");

        assertThat(uploadedFilePage.getMatchCompleteText(), is(equalTo("No line items found")));
    }
}
