package com.apriori.edc.ui.tests.pcba;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.edc.api.utils.BillOfMaterialsUtil;
import com.apriori.edc.ui.pageobjects.login.EdcAppLoginPage;
import com.apriori.edc.ui.pageobjects.login.UploadedFilePage;
import com.apriori.edc.ui.utils.CostStatusEnum;
import com.apriori.edc.ui.utils.EdcUiResources;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.FileResourceUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

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
        BillOfMaterialsUtil.deleteBillOfMaterialByIdUi(EdcUiResources.getBillOfMaterialsId(driver.getCurrentUrl()), currentUser);
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
