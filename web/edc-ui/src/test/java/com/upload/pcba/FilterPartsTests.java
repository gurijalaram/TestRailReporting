package com.upload.pcba;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.edcapi.utils.EDCResources;
import com.apriori.pageobjects.pages.login.EdcAppLoginPage;
import com.apriori.pageobjects.pages.login.MatchedPartPage;
import com.apriori.pageobjects.pages.login.UploadedFilePage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.CostStatusEnum;
import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;

import java.io.File;

public class FilterPartsTests extends TestBase {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private UploadedFilePage uploadedFilePage;
    private MatchedPartPage matchedPartPage;

    public FilterPartsTests() {
        super();
    }

    @After
    public void cleanUp() {
        EDCResources.deleteBillOfMaterialById(uploadedFilePage.getBillOfMaterialsId());
    }

    @Test
    @TestRail(testCaseId = "1727")
    @Description("BOM - Filter Parts - Cost Status - Available Drop Down options")
    public void testFiltering() {

        final CostStatusEnum costStatusEnum = CostStatusEnum.MATCH_COMPLETE;
        String fileName = "Test BOM 5.csv";
        resourceFile = FileResourceUtil.getResourceAsFile(fileName);

        loginPage = new EdcAppLoginPage(driver);
        uploadedFilePage = loginPage.login(UserUtil.getUser())
            .uploadComponent(resourceFile)
            .clickUploadPCBA()
            .filterDropdown()
            .selectSearch("4608")
            .costStatusDropdown()
            .selectCostStatus(costStatusEnum);

        assertThat(uploadedFilePage.getMatchCompleteText(), is(equalTo("No line items found")));
    }
}
