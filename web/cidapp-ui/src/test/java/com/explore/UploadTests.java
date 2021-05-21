package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class UploadTests extends TestBase {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    @Test
    @TestRail(testCaseId = {"5422"})
    @Description("Failed upload of any other types of files")
    public void invalidFile() {

        resourceFile = FileResourceUtil.getResourceAsFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String fileError;

        loginPage = new CidAppLoginPage(driver);
        fileError = loginPage.login(UserUtil.getUser())
            .uploadComponent(testScenarioName, resourceFile).getFileInputError();

        assertThat(fileError, containsString("The file type of the selected file is not supported"));
    }

    @Test
    @TestRail(testCaseId = {"5423"})
    @Description("Nothing uploaded or translated if user select a file but then cancels the new component dialog")
    public void cancelUpload() {

        resourceFile = FileResourceUtil.getResourceAsFile("Piston_assembly.stp");
        String testComponentName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndCancel(testComponentName, resourceFile, ExplorePage.class);

        assertThat(explorePage.getListOfScenarios(testComponentName, "Piston_assembly"), is(equalTo(0)));
    }
}