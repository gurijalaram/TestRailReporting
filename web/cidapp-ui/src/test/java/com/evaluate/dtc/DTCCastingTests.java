package com.evaluate.dtc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.pageobjects.pages.evaluate.EvaluatePage;
import com.apriori.pageobjects.pages.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.pageobjects.pages.evaluate.designguidance.InvestigationPage;
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

public class DTCCastingTests extends TestBase {

    private CidAppLoginPage loginPage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private EvaluatePage evaluatePage;
    private UserCredentials currentUser;

    private File resourceFile;
    private InvestigationPage investigationsPage;

    public DTCCastingTests() {
        super();
    }

    /*@After
    public void resetSettings() {
        if (currentUser != null) {
            new AfterTestUtil().resetAllSettings(currentUser.getUsername());
        }
    }*/

    /*    @Test
    @TestRail(testCaseId = {"6468", "6379", "6383", "6387", "6389", "6391", "6382", "6292"})
    @Description("Testing DTC Casting - Sand Casting")
    public void sandCastingDTC() {
        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        currentUser = UserUtil.getUser();
        String scenarioName = new GenerateStringUtil().generateScenarioName();

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .costScenario(8);

        assertThat(evaluatePage.isDfmRiskIcon("Critical"), is(true));
        assertThat(evaluatePage.isDfmRisk("Critical"), is(true));

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Walls", "CurvedWall:18");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface is below the minimum recommended draft angle."));

        guidanceIssuesPage.selectIssueTypeGcd("Hole Issue", "Maximum Hole Depth", "SimpleHole:2");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. The Hole Depth is greater than the maximum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Hole Issue", "Minimum Hole Diameter", "SimpleHole:10");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. Hole Diameter is less than the minimum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Radius Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));

        guidanceIssuesPage.selectIssueTypeGcd("Machining Issues", "Obstructed Surfaces", "PlanarFace:4");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Facing: Feature is obstructed. Override operation feasibility, select a specialized machining operation, or modify CAD geometry."));
    }

    /*@Category({CustomerSmokeTests.class, SmokeTests.class})
    @Test
    @TestRail(testCaseId = {"1261"})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void geometryTest() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        geometryPage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario(3)
            .openDesignGuidance()
            .openGeometryTab()
            .selectGCDAndGCDProperty("Surfaces", "Planar Faces", "PlanarFace:1");

        evaluatePage = new EvaluatePage(driver);
        propertiesDialogPage = evaluatePage.selectAnalysis()
            .selectProperties()
            .expandDropdown("Properties");
        assertThat(propertiesDialogPage.getProperties("Finished Area (mm2)"), containsString("85.62"));
    }*/

    @Test
    @Category(SmokeTests.class)
    @TestRail(testCaseId = {"6375", "6379", "6384", "6386", "6388", "6390"})
    @Description("Min & Max DTC checks for Die Casted Part")
    public void highPressureDieCasting() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        String componentName = "DTCCastingIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".catpart");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:6");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface is below the minimum recommended draft angle."));
        assertThat(guidanceIssuesPage.getGcdCount("Curved Wall"), equalTo(87));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Minimum Wall Thickness", "Component", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("High Pressure Die Casting is not feasible. Part Thickness is less than the minimum limit with this material."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("High Pressure Die Casting is not feasible. Part Thickness is more than the maximum limit with this material."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radius Issue, Minimum Internal Edge Radius", "Sharp Edge", "SharpEdge:39");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("High Pressure Die Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Minimum Hole Diameter", "Simple Hole", "SimpleHole:13");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("High Pressure Die Casting is not feasible. Hole Diameter is less than the minimum limit with this material."));
    }

    /*@Test
    @TestRail(testCaseId = {"6379", "6384", "6388"})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void gravityDieCasting() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        guidanceIssuesPage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openProcessDetails()
            .selectRoutingsButton()
            .selectRouting("Gravity Die Cast")
            .apply()
            .closePanel()
            .costScenario()
            .openDesignGuidance()
            .openGuidanceTab()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:7");

        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Part of this surface is below the minimum recommended draft angle."));
        guidanceIssuesPage.closePanel();

        guidanceIssuesPage.selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Gravity Die Casting is not feasible. Part Thickness is less than the minimum limit with this material."));
        guidanceIssuesPage.closePanel();

        guidanceIssuesPage.selectIssueTypeGcd("Radius Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Gravity Die Casting is not feasible. Internal Edge Radius is less than the minimum limit with this material."));
    }

    @Test
    @Category({SmokeTests.class, SanityTests.class})
    @TestRail(testCaseId = {"6377"})
    @Description("Validate Tolerance counts are correct")
    public void dtcTolerances() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_DIE;

        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, "DTCCastingIssues.catpart");
        loginPage = new CidAppLoginPage(driver);
        currentUser = UserUtil.getUser();

        toleranceSettingsPage = loginPage.login(currentUser)
            .openSettings()
            .openTolerancesTab()
            .selectUseCADModel();

        settingsPage = new SettingsPage(driver);
        tolerancePage = settingsPage.save(ExplorePage.class)
            .uploadFileAndOk(new GenerateStringUtil().generateScenarioName(), resourceFile, EvaluatePage.class)
            .inputProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.DIAMTOLERANCE.getToleranceName()), "9"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.FLATNESS.getToleranceName()), "5"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.PROFILESURFACE.getToleranceName()), "6"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.ROUGHNESSRA.getToleranceName()), "3"), Matchers.is(true));
        assertThat(tolerancePage.isToleranceCount((ToleranceEnum.STRAIGHTNESS.getToleranceName()), "3"), Matchers.is(true));
    } */

    @Test
    @TestRail(testCaseId = {"6385", "6393", "6394", "8333"})
    @Description("MAX. thickness checks for Sand casting (Al. 1016.0mm MAX.)")
    public void sandCastingDTCIssues() {

        final ProcessGroupEnum processGroupEnum = ProcessGroupEnum.CASTING_SAND;

        String componentName = "SandCastIssues";
        resourceFile = FileResourceUtil.getCloudFile(processGroupEnum, componentName + ".SLDPRT");
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        currentUser = UserUtil.getUser();

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(currentUser)
            .uploadComponentAndOpen(componentName, scenarioName, resourceFile, currentUser)
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial("Aluminum, Cast, ANSI AL380.0")
            .submit()
            .selectProcessGroup(processGroupEnum.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Maximum Hole Depth", "Multi Step Hole", "MultiStepHole:1");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. The Hole Depth is greater than the maximum limit with this material."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue", "Maximum Hole Depth", "SimpleHole:2");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. The Hole Depth is greater than the maximum limit with this material."));

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");
        assertThat(guidanceIssuesPage.getIssueDescription(), containsString("Sand Casting is not feasible. Part Thickness is more than the maximum limit with this material."));
    }
}