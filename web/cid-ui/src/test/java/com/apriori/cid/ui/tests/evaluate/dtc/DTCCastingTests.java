package com.apriori.cid.ui.tests.evaluate.dtc;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cid.api.utils.UserPreferencesUtil;
import com.apriori.cid.ui.pageobjects.evaluate.EvaluatePage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.GuidanceIssuesPage;
import com.apriori.cid.ui.pageobjects.evaluate.designguidance.TolerancesPage;
import com.apriori.cid.ui.pageobjects.evaluate.inputs.AdvancedPage;
import com.apriori.cid.ui.pageobjects.explore.ExplorePage;
import com.apriori.cid.ui.pageobjects.login.CidAppLoginPage;
import com.apriori.cid.ui.utils.EvaluateDfmIconEnum;
import com.apriori.cid.ui.utils.ToleranceEnum;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.dataservice.ComponentRequestUtil;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DTCCastingTests extends TestBaseUI {

    private SoftAssertions softAssertions = new SoftAssertions();
    private CidAppLoginPage loginPage;
    private GuidanceIssuesPage guidanceIssuesPage;
    private EvaluatePage evaluatePage;
    private ExplorePage explorePage;
    private TolerancesPage tolerancesPage;
    private ComponentInfoBuilder component;

    public DTCCastingTests() {
        super();
    }

    @AfterEach
    public void resetAllSettings() {
        if (component != null) {
            new UserPreferencesUtil().resetSettings(component.getUser());
        }
    }

    @Test
    @Issue("APD-1286")
    @TestRail(id = {6468, 6379, 6383, 6389, 6382, 6292})
    @Description("Testing DTC Casting - Sand Casting")
    public void sandCastingDTC() {
        component = new ComponentRequestUtil().getComponentByProcessGroup(ProcessGroupEnum.CASTING_SAND);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .costScenario(8);

        softAssertions.assertThat(evaluatePage.getDfmRiskIcon()).isEqualTo(EvaluateDfmIconEnum.CRITICAL.getIcon());
        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("Critical");

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:18");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Part of this surface has a draft angle less than the recommended draft angle for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Hole Issue", "Maximum Hole Depth", "SimpleHole:2");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole depth is greater than the recommended depth for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Hole Issue", "Minimum Hole Diameter", "SimpleHole:10");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole diameter is less than the recommended minimum diameter for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Radius Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6375, 6379, 6384, 6386, 6388, 6390})
    @Description("Min & Max DTC checks for Die Casted Part")
    public void highPressureDieCasting() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("High Pressure Die Cast")
            .submit(AdvancedPage.class)
            .goToBasicTab()
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:6");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Part of this surface has a draft angle less than the recommended draft angle for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdCount("Curved Wall")).isEqualTo(87);

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Minimum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Radius Issue, Minimum Internal Edge Radius", "Sharp Edge", "SharpEdge:39");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Minimum Hole Diameter", "Simple Hole", "SimpleHole:13");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole diameter is less than the recommended minimum diameter for this material.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6379, 6384, 6388})
    @Description("Ensure that the Geometry tab section is expandable table of GCDs to third hierarchical level with total at GCD type level")
    public void gravityDieCasting() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues",ProcessGroupEnum.STOCK_MACHINING);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Minimum Wall Thickness", "Component:1");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Minimum wall thickness is less than the recommended thickness for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Radius Issue", "Minimum Internal Edge Radius", "SharpEdge:38");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Internal edge radius is less than the recommended internal edge radius for this material.");

        guidanceIssuesPage.selectIssueTypeGcd("Draft Issue, Draft Angle", "Curved Wall", "CurvedWall:7");
        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Part of this surface has a draft angle less than the recommended draft angle for this material.");
        softAssertions.assertAll();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {6377})
    @Description("Validate Tolerance counts are correct")
    public void dtcTolerances() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.CASTING_DIE);

        loginPage = new CidAppLoginPage(driver);
        tolerancesPage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class)
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .costScenario()
            .openDesignGuidance()
            .openTolerancesTab();

        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.DIAMTOLERANCE)).isEqualTo(9);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.FLATNESS)).isEqualTo(5);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.PROFILESURFACE)).isEqualTo(6);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRA)).isEqualTo(3);
        softAssertions.assertThat(tolerancesPage.getGcdCount(ToleranceEnum.ROUGHNESSRA)).isEqualTo(3);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6385, 6393, 6394, 8333, 6469})
    @Description("MAX. thickness checks for Sand casting (Al. 1016.0mm MAX.)")
    public void sandCastingDTCIssues() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("SandCastIssues", ProcessGroupEnum.CASTING_SAND);

        loginPage = new CidAppLoginPage(driver);
        evaluatePage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(component.getProcessGroup())
            .openMaterialSelectorTable()
            .search("ANSI AL380")
            .selectMaterial(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName())
            .submit(EvaluatePage.class)
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("ManualFloor")
            .submit(EvaluatePage.class)
            .costScenario()
            .costScenario();

        softAssertions.assertThat(evaluatePage.getDfmRisk()).isEqualTo("High");

        guidanceIssuesPage = evaluatePage.openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue, Maximum Hole Depth", "Multi Step Hole", "MultiStepHole:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole depth is greater than the recommended depth for this material.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Hole Issue", "Maximum Hole Depth", "SimpleHole:2");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Hole depth is greater than the recommended depth for this material.");

        guidanceIssuesPage.closePanel()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue", "Maximum Wall Thickness", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Maximum wall thickness is greater than the recommended thickness for this material.");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6488})
    @Description("Failures/warnings tab - Verify costing failures are highlighted within the Design Guidance details tile Warnings tab with useful error message")
    public void errorMessagesInDesignGuidanceTab() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        explorePage = loginPage.login(component.getUser())
            .openSettings()
            .goToToleranceTab()
            .selectCad()
            .submit(ExplorePage.class);

        guidanceIssuesPage = explorePage
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario(4)
            .openDesignGuidance()
            .selectIssueType("Not Supported GCDs", "Detached Solid");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).contains("Multiple bodies exist in the model.  Only the largest body is used and the remainder are ignored.");

        guidanceIssuesPage.selectIssueTypeGcd("Failed GCDs", "Failed to cost", "CurvedWall:100");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).isEqualTo("High Pressure Die Casting is not feasible. It is incapable of achieving [Diam Tolerance : 0.002 mm (0.0001 in); best achievable for this feature is 0.1335 mm (0.0053 in)].");

        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = {6387})
    @Description("MAX. thickness checks for Die casting-Al. 38.1mm MAX. for high pressure, 50.5mm MAX. for gravity die casting")
    public void maxThicknessForDieCasting() {
        component = new ComponentRequestUtil().getComponentWithProcessGroup("DTCCastingIssues", ProcessGroupEnum.PLASTIC_MOLDING);

        loginPage = new CidAppLoginPage(driver);
        guidanceIssuesPage = loginPage.login(component.getUser())
            .uploadComponentAndOpen(component)
            .selectProcessGroup(ProcessGroupEnum.CASTING_DIE)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).isEqualTo("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("<= 38.10mm");

        guidanceIssuesPage = guidanceIssuesPage.closePanel()
            .goToAdvancedTab()
            .openRoutingSelection()
            .selectRoutingPreferenceByName("Gravity Die Cast")
            .submit(EvaluatePage.class)
            .costScenario()
            .openDesignGuidance()
            .selectIssueTypeGcd("Material Issue, Maximum Wall Thickness", "Component", "Component:1");

        softAssertions.assertThat(guidanceIssuesPage.getIssueDescription()).isEqualTo("Maximum wall thickness is greater than the recommended thickness for this material.");
        softAssertions.assertThat(guidanceIssuesPage.getGcdSuggested("Component:1")).contains("<= 50.50mm");

        softAssertions.assertAll();
    }
}