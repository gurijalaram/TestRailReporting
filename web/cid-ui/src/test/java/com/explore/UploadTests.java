package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.pageobjects.pages.explore.ExplorePage;
import com.pageobjects.pages.explore.FileOpenError;
import com.pageobjects.pages.login.CidLoginPage;
import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {
    private CidLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"575"})
    @Description("Failed upload of any other types of files")
    public void invalidFile() {

        resourceFile = FileResourceUtil.getResourceAsFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadFile(testScenarioName, resourceFile);

        assertThat(new FileOpenError(driver).getErrorText(), containsString("The selected file type is not supported"));
    }

    @Test
    @TestRail(testCaseId = {"576"})
    @Description("Nothing uploaded or translated if user select a file but then cancels the new component dialog")
    public void cancelUpload() {

        resourceFile = FileResourceUtil.getCloudFile(ProcessGroupEnum.ASSEMBLY, "Piston_assembly.stp");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadFileAndCancel(testScenarioName, resourceFile, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testScenarioName, "Piston_assembly"), is(equalTo(0)));
    }
}