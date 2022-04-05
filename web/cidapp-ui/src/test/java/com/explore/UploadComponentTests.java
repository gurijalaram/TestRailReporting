package com.explore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.pageobjects.pages.explore.CadFileStatusPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.explore.ImportCadFilePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import com.utils.ColumnsEnum;
import com.utils.MultiUpload;
import com.utils.SortOrderEnum;
import com.utils.UploadStatusEnum;
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SanityTests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadComponentTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private UserCredentials currentUser;
    private CadFileStatusPage cadFileStatusPage;
    private ImportCadFilePage importCadFilePage;

    @Test
    @Category(SanityTests.class)
    @Description("Test uploading a component")
    public void testUploadComponent() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING;

        final String componentName = "Case_17";
        final String extension = ".stp";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .close()
            .clickSearch(componentName)
            .sortColumn(ColumnsEnum.CREATED_AT, SortOrderEnum.DESCENDING);

        assertThat(explorePage.getListOfScenarios(componentName, scenarioName), is(equalTo(1)));
    }

    @Test
    @TestRail(testCaseId = "11879")
    @Description("Validate messaging upon successful upload of multiple files")
    public void testDisabledScenarioNameTextBox() {
        UserCredentials currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        importCadFilePage = loginPage.login(currentUser)
            .importCadFile()
            .inputScenarioName(scenarioName)
            .inputMultiComponents(multiComponents, UploadStatusEnum.UPLOADED);

        importCadFilePage.scenarioNameTextBoxDisabled().forEach(textBox -> assertThat(textBox, is("true")));

        cadFileStatusPage = importCadFilePage.submit();

        assertThat(cadFileStatusPage.getImportMessage(), is(containsString(String.format("%s file(s) imported successfully.", multiComponents.size()))));
    }

    @Test
    @TestRail(testCaseId = "CIS-304")
    @Description("Validate multi-upload through explorer menu")
    public void multiUploadTests() {
        currentUser = UserUtil.getUser();
        List<MultiUpload> multiComponents = new ArrayList<>();
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.SHEET_METAL, "bracket_basic.prt"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.POWDER_METAL, "PowderMetalShaft.stp"), new GenerateStringUtil().generateScenarioName()));
        multiComponents.add(new MultiUpload(FileResourceUtil.getCloudFile(ProcessGroupEnum.PLASTIC_MOLDING, "Push Pin.stp"), new GenerateStringUtil().generateScenarioName()));

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .importCadFile()
            .unTick("Apply to all")
            .inputMultiComponentDetails(multiComponents)
            .submit()
            .close();

        multiComponents.forEach(component -> assertThat(explorePage.getListOfScenarios(component.getResourceFile().getName().split("\\.")[0], component.getScenarioName()), is(equalTo(0))));
    }
}
