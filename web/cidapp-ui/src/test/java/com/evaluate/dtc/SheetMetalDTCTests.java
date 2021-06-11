package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.apibase.utils.AfterTestUtil;
import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.login.CidAppLoginPage;
import com.apriori.utils.FileResourceUtil;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.users.UserCredentials;
import com.apriori.utils.users.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import testsuites.suiteinterface.SmokeTests;

import java.io.File;

public class SheetMetalDTCTests extends TestBase {

    private CidAppLoginPage loginPage;
    private EvaluatePage evaluatePage;
    private GuidanceIssuesPage guidanceIssuesPage;

    private UserCredentials currentUser;
    private File resourceFile;

    public SheetMetalDTCTests() {
        super();
    }

    //@After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Min Diameter", "Simple Hole", "SimpleHole:2");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("The hole can not be made by a Plasma Cutting operation on the Plasma Cut process as the kerf width is too small"));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Hole - Blind", "Simple Hole", "SimpleHole:4");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("The GCD failed feasibility for Plasma Cutting as this process cannot be used to manufacture a blind hole"));

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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario(3)
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Complex Hole", "ComplexHole:14");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Increase the distance between the holes to the suggested value."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Proximity Warning, Hole - Hole Proximity", "Simple Hole", "SimpleHole:2");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Increase the distance between the holes to the suggested value."));
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Bend Issue, Bend - Intersects Form", "Straight Bend", "StraightBend:4");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("The intersection between the bend and form cannot be accessed by the tool when using Bending"));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance().selectIssueTypeGcd("Bend Issue, Bend - Min Flap Size", "Straight Bend", "StraightBend:11");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("The short bend flap will be difficult to form or will require extra processing to trim after bending"));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance().selectIssueTypeGcd("Bend Issue, Bend - Min Radius", "Straight Bend", "StraightBend:4");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("the radius is too small for the machine capability"));
    }

    @Test
    @Category(SmokeTests.class)
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
            /*.openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        evaluatePage = settingsPage.save(ExplorePage.class)*/
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario();

        assertThat(evaluatePage.getGuidanceResult("Design Warnings"), is("15"));
        assertThat(evaluatePage.getGuidanceResult("Design Failures"), is("4"));
        //assertThat(evaluatePage.getGuidanceResult("GCDs with Tolerances"), is("22"));
    }

    /*@Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"1834", "1835", "1836", "1837"})
    @Description("Testing DTC Sheet Metal")
    public void sheetMetalDTCInvestigation() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.SHEET_METAL;

        String componentName = "SheMetDTC";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        investigationPage = settingsPage.save(ExplorePage.class)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openInvestigationTab()
            .selectInvestigationTopic("Holes and Fillets");

        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "Tool Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("Hole - Standard", "GCD Count"), is(equalTo("4")));

        investigationPage.selectInvestigationTopic("Distinct Sizes Count");

        assertThat(investigationPage.getInvestigationCell("Bend Radius", "Tool Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Bend Radius", "GCD Count"), is(equalTo("1")));
        assertThat(investigationPage.getInvestigationCell("Hole Size", "Tool Count"), is(equalTo("2")));
        assertThat(investigationPage.getInvestigationCell("Hole Size", "GCD Count"), is(equalTo("4")));

        investigationPage.selectInvestigationTopic("Machining Setups");

        assertThat(investigationPage.getInvestigationCell("SetupAxis:1", "GCD Count"), is(equalTo("14")));
    }*/

    @Test
    //TODO update testrail case 719 when editing tolerances are ported
    @Ignore("Requires tolerances for additional operation")
    @Category(SmokeTests.class)
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
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("AISI 1020")
            .selectMaterial("Steel, Cold Worked, AISI 1020")
            .submit()
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("GCDs With Special Finishing", "Reaming", "SimpleHole:2");

        assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2"), is(equalTo(0.02)));
        assertThat(guidanceIssuesPage.getGcdCurrent("SimpleHole:2"), is(equalTo(0.06)));
    }
}
