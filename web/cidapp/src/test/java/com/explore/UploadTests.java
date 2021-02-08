package com.explore;

import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.web.driver.TestBase;

import java.io.File;

public class UploadTests extends TestBase {
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;

    private File resourceFile;

    /*@Test
    @TestRail(testCaseId = {"5422"})
    @Description("Failed upload of any other types of files")
    public void invalidFile() {

        resourceFile = FileResourceUtil.getResourceAsFile("InvalidFileType.txt");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        loginPage.login(UserUtil.getUser())
            .uploadComponent(testScenarioName, resourceFile);

        assertThat(new FileOpenError(driver).getErrorText(), containsString("The selected file type is not supported"));
    }*/

    /*@Test
    @TestRail(testCaseId = {"5423"})
    @Description("Nothing uploaded or translated if user select a file but then cancels the new component dialog")
    public void cancelUpload() {

        resourceFile = FileResourceUtil.getResourceAsFile("Piston_assembly.stp");
        String testComponentName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndCancel(testComponentName, resourceFile, ExplorePage.class);

        assertThat(explorePage.getListOfComponents(testComponentName, "Piston_assembly"), is(equalTo(0)));
    }*/
}