package com.apriori.pcba;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.edc.utils.BillOfMaterialsUtil;
import com.apriori.http.utils.FileResourceUtil;
import com.apriori.pageobjects.login.EdcAppLoginPage;
import com.apriori.pageobjects.login.UploadedFilePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testconfig.TestBaseUI;
import com.apriori.testrail.TestRail;

import com.utils.CostStatusEnum;
import com.utils.EdcUiResources;
import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FilterPartsTests extends TestBaseUI {

    private File resourceFile;
    private EdcAppLoginPage loginPage;
    private UploadedFilePage uploadedFilePage;
    private UserCredentials currentUser;

    public FilterPartsTests() {
        super();
    }

    @AfterEach
    public void cleanUp() {
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()));
    }

    @Test
    @TestRail(id = 1727)
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