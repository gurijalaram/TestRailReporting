package com.evaluate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.utils.AssemblyUtils;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
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
import com.utils.SortOrderEnum;
import com.utils.UploadStatusEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.RegressionTestSuite;
import testsuites.suiteinterface.SanityTests;

import java.io.File;

public class OpenUnknownComponentsTests extends TestBase {

    private File resourceFile;
    private CidAppLoginPage loginPage;
    private ExplorePage explorePage;
    private UserCredentials currentUser;
    private CadFileStatusPage cadFileStatusPage;
    private ImportCadFilePage importCadFilePage;
    private EvaluatePage evaluatePage;
    private ComponentInfoBuilder cidComponentItem;
    private static ComponentInfoBuilder componentAssembly;
    private static AssemblyUtils assemblyUtils = new AssemblyUtils();
    private SoftAssertions softAssertions = new SoftAssertions();

    @Test
    @Category(RegressionTestSuite.class)
    @TestRail(testCaseId = "14203")
    @Description("Test opening an 'unknown' component immediately after upload")
    public void testOpenUnknownPart() {
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
            .close();

        assertThat(explorePage.getScenarioState(componentName.toUpperCase(), scenarioName), is(equalTo("gear")));

        evaluatePage = explorePage.openScenario(componentName, scenarioName);
    }

    @Test
    @Category(RegressionTestSuite.class)
    @TestRail(testCaseId = "14204")
    @Description("Test opening an 'unknown' component immediately after upload")
    public void testOpenUnknownAssembly() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.ASSEMBLY;

        final String componentName = "top-level";
        final String extension = ".asm.1";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + extension);
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(UserUtil.getUser())
            .importCadFile()
            .inputComponentDetails(scenarioName, resourceFile)
            .waitForUploadStatus(componentName + extension, UploadStatusEnum.UPLOADED)
            .submit()
            .close();

        assertThat(explorePage.getScenarioState(componentName.toUpperCase(), scenarioName), is(equalTo("gear")));

        evaluatePage = explorePage.openScenario(componentName, scenarioName);
    }
}
