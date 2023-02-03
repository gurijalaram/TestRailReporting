package com.evaluate.dtc;

import com.apriori.cidappapi.utils.UserPreferencesUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.InvestigationPage;
import com.apriori.pageobjects.pages.explore.ExplorePage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.pageobjects.pages.settings.ToleranceDefaultsPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.IgnoreTests;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SheetMetalDTCTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private ToleranceDefaultsPage toleranceDefaultsPage;
    private ExplorePage explorePage;
    private InvestigationPage investigationPage;
    SoftAssertions softAssertions = new SoftAssertions();

    private UserCredentials currentUser;
    private File resourceFile;

    public SheetMetalDTCTests() {
        super();
    }

    @After
    public void resetAllSettings() {
        if (currentUser != null) {
            new UserPreferencesUtil().resetSettings(currentUser);
        }
    }

    @Test
    @TestRail(testCaseId = {"6496", "6499", "6500"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCHoles() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "SheMetDTC";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);

        guidanceIssuesPage = loginPage.login(currentUser)
            /*.openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidanceIssuesPage = settingsPage.save(ExplorePage.class)*/
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Min Diameter", "Simple Hole", "SimpleHole:2");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole cannot be made by a Plasma Cutting operation on the Plasma Cut process as the kerf width is too large.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Blind", "Simple Hole", "SimpleHole:4");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("SimpleHole:4 is blind and Plasma Cutting cannot make blind holes.");

        softAssertions.assertAll();

        /*guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Facing", "PlanarFace:13");
        assertThat(guidanceIssuesPage.getGcdCount("Facing"), is(equalTo("1")));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Center Drilling / Pecking", "SimpleHole:2");
        assertThat(guidanceIssuesPage.getGcdCount("Center Drilling / Pecking"), is(equalTo("1")));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Machined GCDs", "Center Drilling / Drilling", "SimpleHole:3");
        assertThat(guidanceIssuesPage.getGcdCount("Center Drilling / Drilling"), is(equalTo("3")));*/
    }

    @Test
    @TestRail(testCaseId = {"6497", "6498"})
    @Description("Verify Proximity Issues Are Highlighted")
    public void sheetMetalProximity() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "SheetMetalTray";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(3)
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Complex Hole", "ComplexHole:14");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The thin strip of material between the holes is at risk of damage from forces during manufacture or service.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Simple Hole", "SimpleHole:2");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The thin strip of material between the holes is at risk of damage from forces during manufacture or service.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"6495", "6501"})
    @Description("Verify Bend Issues Are Highlighted")
    public void sheetMetalBends() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "extremebends";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Intersects Form", "Straight Bend", "StraightBend:4");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("The intersection between the bend and form cannot be accessed by the tool when using Bending");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Min Flap Size", "Straight Bend", "StraightBend:11");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Bend flap is too short to be easily made with standard bending operations.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Min Radius", "Straight Bend", "StraightBend:4");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Bend radius is too small for the machine capability.");

        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @Issue("COST-685")
    @TestRail(testCaseId = {"6486"})
    @Description("Verify the Design Guidance tile presents the correct counts for number of GCDs, warnings, guidance issues, & tolerances for a part")
    public void tileDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "extremebends";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario();

        softAssertions.assertThat(evaluatePage.getGuidanceResult("Design Warnings")).isEqualTo("18");
        softAssertions.assertThat(evaluatePage.getGuidanceResult("Design Failures")).isEqualTo("4");
        softAssertions.assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances")).isEqualTo("22");

        softAssertions.assertAll();
    }

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1834", "1835", "1836", "1837", "6491", "6492", "6493", "6494"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCInvestigation() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "SheMetDTC";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(currentUser)
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class);

        investigationPage = explorePage
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.SHEET_METAL)
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectTopic("Holes and Fillets");

        softAssertions.assertThat(investigationPage.getGcdCount("Hole - Standard (2 Tools)")).isEqualTo(4);

        investigationPage.selectTopic("Distinct Sizes Count");

        softAssertions.assertThat(investigationPage.getGcdCount("Bend Radius (1 Tool)")).isEqualTo(1);
        softAssertions.assertThat(investigationPage.getGcdCount("Hole Size (2 Tools)")).isEqualTo(4);

        investigationPage.selectTopic("Machining Setups");

        softAssertions.assertThat(investigationPage.getGcdCount("SetupAxis:1")).isEqualTo(14);

        softAssertions.assertAll();
    }

    @Test
    //TODO update testrail case 719 when editing tolerances are ported
    @Ignore("Requires tolerances for additional operation")
    @Category({SmokeTests.class, IgnoreTests.class})
    @TestRail(testCaseId = {"6502", "719"})
    @Description("Verify tolerances which induce an additional operation")
    public void toleranceAdditionalOp() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "bracket_basic_matPMI";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".prt.1");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            /*.openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidanceIssuesPage = settingsPage.save(ExplorePage.class)*/
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum)
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial(MaterialNameEnum.STEEL_COLD_WORKED_AISI1020.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("GCDs With Special Finishing", "Reaming", "SimpleHole:2");

        softAssertions.assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2")).isEqualTo(0.02);
        softAssertions.assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2")).isEqualTo(0.06);

        softAssertions.assertAll();
    }
}
