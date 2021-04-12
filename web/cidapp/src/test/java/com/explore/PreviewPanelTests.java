package com.explore;

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
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Test;

import java.io.File;

public class PreviewPanelTests extends TestBase {

    private CidAppLoginPage loginPage;
    private PreviewPage previewPage;

    private File resourceFile;

    public PreviewPanelTests() {
        super();
    }

    @Test
    @Description("Test preview panel data is displayed")
    @TestRail(testCaseId = {"1102", "1103"})
    public void testPreviewPanelDisplay() {

        String partName = "Casting";
        String testScenarioName = new GenerateStringUtil().generateScenarioName();
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, partName + ".prt");

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .highlightScenario(testScenarioName, partName)
            .previewPanel();

        assertThat(previewPage.isPreviewPanelDisplayed(), is(true));
    }

    @Test
    @Description("Validate user can see information and metrics for the selected scenario in the preview panel")
    @TestRail(testCaseId = {"1104", "1105"})
    public void previewPanelMetrics() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "225_gasket-1-solid1.prt.1");
        String testScenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        previewPage = loginPage.login(UserUtil.getUser())
            .uploadComponentAndSubmit(testScenarioName, resourceFile, EvaluatePage.class)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .clickExplore()
            .highlightScenario(testScenarioName, "225_gasket-1-solid1")
            .previewPanel();

        assertThat(previewPage.isImageDisplayed(), is(true));
        assertThat(previewPage.getMaterialResult("Piece Part Cost"), closeTo(0.91, 1));
        assertThat(previewPage.getMaterialResult("Fully Burdened Cost"), closeTo(1.64, 1));
        assertThat(previewPage.getMaterialResult("Total Capital Investment"), closeTo(20057.85, 1));
    }
}
