package com.explore;

import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class MultiUploadTests extends TestBase {

    private File resourceFile;
    private UserCredentials currentUser;
    private CidAppLoginPage loginPage;
    private ImportCadFilePage importCadFilePage;

    @Test
    @Description()
    public void multiupload() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic.prt";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName);
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .multiUploadComponent(scenarioName, resourceFile);
    }
}
