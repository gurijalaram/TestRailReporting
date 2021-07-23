package com.explore;

import static com.apriori.utils.enums.ProcessGroupEnum.PLASTIC_MOLDING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.explore.PreviewPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class PreviewPanelTests extends TestBase {

    private CidAppLoginPage loginPage;
    private PreviewPage previewPage;

    private File resourceFile;
    UserCredentials currentUser;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(testCaseId = {"6350", "6349"})
    public void testPreviewPanelDisplay() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String partName = "Casting";
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".prt");
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(partName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .search("ABS, 10")
            .selectMaterial("ABS, 10% Glass")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .highlightScenario(partName, testScenarioName)
            .previewPanel();

        assertThat(previewPage.isPreviewPanelDisplayed(), is(true));
    }

    @Test
    @Category(SmokeTests.class)
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(testCaseId = {"6351", "6201", "6352"})
    public void previewPanelMetrics() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        String componentName = "225_gasket-1-solid1";
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, testScenarioName, resourceFile, currentUser)
            .selectProcessGroup(PLASTIC_MOLDING)
            .openMaterialSelectorTable()
            .search("ABS, 10")
            .selectMaterial("ABS, 10% Glass")
            .submit(EvaluatePage.class)
            .costScenario()
            .clickExplore()
            .selectFilter("Recent")
            .clickSearch(componentName)
            .highlightScenario(componentName, testScenarioName)
            .previewPanel();

        assertThat(previewPage.isImageDisplayed(), is(true));
        assertThat(previewPage.getMaterialResult("Piece Part Cost"), closeTo(0.48, 1));
        assertThat(previewPage.getMaterialResult("Fully Burdened Cost"), closeTo(0.86, 1));
        assertThat(previewPage.getMaterialResult("Total Capital Investment"), closeTo(10526.66, 2));
    }
}
